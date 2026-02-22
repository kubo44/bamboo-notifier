package org.romika.bamboonotifier.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BuildCheckerScheduler {

	private final BuildCheckerService buildCheckerService;

	public BuildCheckerScheduler(BuildCheckerService buildCheckerService) {
		this.buildCheckerService = buildCheckerService;
	}

	@Scheduled(fixedRateString = "#{bambooProperties.checkIntervalMinutes * 60 * 1000}")
	public void scheduledCheckBuilds() {
		buildCheckerService.checkBuilds();
	}
}