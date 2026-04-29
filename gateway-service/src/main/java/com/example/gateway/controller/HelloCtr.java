package com.example.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HelloCtr {


    /**
     * 首页：显示所有文件列表
     */
    @GetMapping("/")
    public String index() throws IOException {

        return "hello";
    }


}
