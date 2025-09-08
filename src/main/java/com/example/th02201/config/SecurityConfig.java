// src/main/java/vn/hoctotlamhay/datn/config/SecurityConfig.java

package com.example.th02201.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Đánh dấu đây là lớp cấu hình của Spring
public class SecurityConfig {

    @Bean // Đánh dấu phương thức này sẽ tạo ra một Spring Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Trả về một instance của BCryptPasswordEncoder
    }

    // BỎ COMMENT TOÀN BỘ PHƯƠNG THỨC filterChain NÀY
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF cho API (nên bật cho các form HTML)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").permitAll() // Cho phép tất cả truy cập vào các API bắt đầu bằng /api/
                        .anyRequest().authenticated() // Mọi request khác đều cần xác thực
                );
        return http.build();
    }

}
