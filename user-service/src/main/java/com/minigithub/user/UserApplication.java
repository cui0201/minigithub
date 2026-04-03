package com.minigithub.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.minigithub.user", "com.minigithub.common"})
@EnableDiscoveryClient
@MapperScan("com.minigithub.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        System.out.println("====================================");
        System.out.println("  User Service 启动成功！");
        System.out.println("  端口: 8081");
        System.out.println("====================================");
    }
}
