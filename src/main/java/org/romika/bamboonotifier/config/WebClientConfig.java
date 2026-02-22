package org.romika.bamboonotifier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	@Bean
	public WebClient bambooWebClient(BambooProperties properties) {
		return WebClient.builder()
				.baseUrl(properties.getUrl())
				.defaultHeaders(headers -> headers.setBearerAuth(properties.getToken()))
				.build();
	}
}
