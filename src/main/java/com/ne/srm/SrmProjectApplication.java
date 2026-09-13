package com.ne.srm;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SrmProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrmProjectApplication.class, args);
    }

    @Component
    public static class ServerInfoPrinter {

        @Value("${server.port}")
        private int port;

        @Value("${server.servlet.context-path:}")
        private String contextPath;

        @Value("${server.address:localhost}")
        private String serverAddress;

        @PostConstruct
        public void printServerInfo() {
            String baseUrl = "http://" + serverAddress + ":" + port + contextPath;
            String line = "=".repeat(40);
            System.out.println(line);
            System.out.println("✅ 应用启动成功！");
            System.out.println("📝 访问地址: " + baseUrl);
            if (!contextPath.isEmpty()) {
                System.out.println("📝 上下文路径: " + contextPath);
            }
            System.out.println("📝 开放端口: " + port);
            System.out.println(line);
        }
    }

}
