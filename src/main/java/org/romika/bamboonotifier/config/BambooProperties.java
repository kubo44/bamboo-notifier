package org.romika.bamboonotifier.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "bamboo")
@Data
@Validated
public class BambooProperties {
	@NotBlank
	private String url;
	@NotBlank
	private String token;
	@NotBlank
	private String projects;
	private int checkIntervalMinutes = 15;
	private int maxPlans = 500;
}