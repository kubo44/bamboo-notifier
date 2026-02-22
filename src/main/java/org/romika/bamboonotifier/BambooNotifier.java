package org.romika.bamboonotifier;

import org.jspecify.annotations.NullMarked;
import org.romika.bamboonotifier.service.BuildCheckerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class BambooNotifier implements CommandLineRunner {

	private final BuildCheckerService buildCheckerService;

	public BambooNotifier(BuildCheckerService buildCheckerService) {
		this.buildCheckerService = buildCheckerService;
	}

	public static void main(String[] args) {
		// Deactivate headless to have SystemTray support
		System.setProperty("java.awt.headless", "false");

		SpringApplication app = new SpringApplication(BambooNotifier.class);
		app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
		app.run(args);
	}

	@Override
	@NullMarked
	public void run(String... args) {
		// first run after start
		buildCheckerService.checkBuilds();
	}
}