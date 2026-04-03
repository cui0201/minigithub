package com.minigithub.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.minigithub.blog", "com.minigithub.common"})
@EnableDiscoveryClient
@MapperScan("com.minigithub.blog.mapper")
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        System.out.println("====================================");
        System.out.println("  Blog Service 启动成功！");
        System.out.println("  端口: 8082");
        System.out.println("====================================");
    }
}
