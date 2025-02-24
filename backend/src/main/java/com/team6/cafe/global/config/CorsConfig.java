package com.team6.cafe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // 모든 API 허용
					.allowedOrigins("http://localhost:3000") // 프론트엔드 URL 허용
					.allowedMethods("GET", "POST", "PATCH", "DELETE") // 허용할 HTTP 메서드
					.allowedHeaders("*")
					.allowCredentials(true);
			}
		};
	}
}

