package com.starlab.withreact.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 관련 설정을 지정해준다.
 * 프론트 서버와 백엔드 서버가 다른 경우 요청을 처리하지 않는 문제를 해결해야 한다.
 */

@Configuration // 스프링 빈으로 등록하기
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로에 대해 cors 정책 처리
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 프론트 서버 Origin 지정
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 모든 HTTP 요청 메서드 허용
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
