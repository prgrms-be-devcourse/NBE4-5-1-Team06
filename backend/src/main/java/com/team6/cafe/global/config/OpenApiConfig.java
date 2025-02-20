package com.team6.cafe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openApi() {
		Info info = new Info()
			.title("Team6 API Document");

		return new OpenAPI()
			.addServersItem(new Server().url("/"))
			.info(info);
	}
}
