package com.example.jarserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class JarFileController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 确保上传目录存在
     */
    private Path getUploadPath() throws IOException {
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        return path;
    }

    /**
     * 首页：显示所有文件列表
     */
    @GetMapping("/")
    public String index(Model model) throws IOException {
        Path uploadPath = getUploadPath();
        List<String> files = Files.list(uploadPath)
                .filter(Files::isRegularFile)
                .map(path -> path.getFileName().toString())
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute("files", files);
        return "index";
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) throws IOException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择一个文件");
            return "redirect:/";
        }

        String originalFilename = file.getOriginalFilename();
        // 简单安全检查：防止路径遍历
        if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
            redirectAttributes.addFlashAttribute("message", "文件名包含非法字符");
            return "redirect:/";
        }

        Path uploadPath = getUploadPath();
        Path targetPath = uploadPath.resolve(originalFilename).normalize();
        // 确保目标路径仍在 uploadPath 内
        if (!targetPath.startsWith(uploadPath)) {
            redirectAttributes.addFlashAttribute("message", "非法文件路径");
            return "redirect:/";
        }

        // 如果文件已存在，可以选择覆盖或报错。这里简单覆盖
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        redirectAttributes.addFlashAttribute("message", "上传成功: " + originalFilename);
        return "redirect:/";
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        Path uploadPath = getUploadPath();
        Path filePath = uploadPath.resolve(filename).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new FileNotFoundException("非法路径");
        }

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            throw new FileNotFoundException("文件不存在: " + filename);
        }

        // 处理中文文件名乱码
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }

    /**
     * 删除文件
     */
    @PostMapping("/delete/{filename}")
    public String deleteFile(@PathVariable String filename, RedirectAttributes redirectAttributes) throws IOException {
        Path uploadPath = getUploadPath();
        Path filePath = uploadPath.resolve(filename).normalize();
        if (!filePath.startsWith(uploadPath)) {
            redirectAttributes.addFlashAttribute("message", "非法文件路径");
            return "redirect:/";
        }

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            redirectAttributes.addFlashAttribute("message", "删除成功: " + filename);
        } else {
            redirectAttributes.addFlashAttribute("message", "文件不存在: " + filename);
        }
        return "redirect:/";
    }
}
