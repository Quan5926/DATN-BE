package com.example.th02201.config; // Đặt trong package config hoặc tương tự

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Cấu hình để phục vụ tài nguyên tĩnh (ảnh) từ thư mục upload
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ URL "/images/**" đến thư mục vật lý "uploads/images/"
        // .toUri().toString() đảm bảo đường dẫn được định dạng đúng cho URI
        String uploadPath = Paths.get("uploads/images/").toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
        System.out.println("Configured static resource handler for /images/** mapping to: " + uploadPath);
    }
}
