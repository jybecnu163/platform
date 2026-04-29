package com.example.orderservice;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(OrderRepository orderRepository) {
        return args -> {
            if (orderRepository.count() == 0) {
                orderRepository.save(new Order(null, "笔记本电脑", 2, new BigDecimal("5999.99")));
                orderRepository.save(new Order(null, "无线鼠标", 5, new BigDecimal("89.90")));
                orderRepository.save(new Order(null, "机械键盘", 1, new BigDecimal("399.00")));
                System.out.println("测试数据初始化完成");
            }
        };
    }
}