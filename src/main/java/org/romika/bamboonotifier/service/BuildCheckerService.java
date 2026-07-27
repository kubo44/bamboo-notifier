package org.romika.bamboonotifier.service;

import lombok.extern.slf4j.Slf4j;
import org.romika.bamboonotifier.config.BambooProperties;
import org.romika.bamboonotifier.model.BambooPlan;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BuildCheckerService {

    private final TrayService trayService;

    private final BambooClient bambooClient;
    private final BambooProperties bambooProperties;

    public BuildCheckerService(
            BambooClient bambooClient,
            BambooProperties bambooProperties,
            TrayService trayService) {
        this.bambooClient = bambooClient;
        this.bambooProperties = bambooProperties;
        this.trayService = trayService;
    }

    public void checkBuilds() {
        List<BambooPlan> plans = bambooClient.fetchPlans(bambooProperties.getMaxPlans());
        Map<String, List<String>> projectToKeys = buildProjectToKeys(plans);
        Map<String, String> keyToBuildState = fetchBuildStates(projectToKeys);
        printAndNotifyFailedBuilds(projectToKeys, keyToBuildState);
    }

    private Map<String, List<String>> buildProjectToKeys(List<BambooPlan> plans) {
        String[] projects = bambooProperties.getProjects().split(",");
        return Arrays.stream(projects)
                .map(String::trim)
                .collect(
                        Collectors.toMap(
                                project -> project,
                                project -> plans.stream()
                                        .filter(plan -> plan.getName() != null)
                                        .filter(plan -> plan.getName().startsWith(project))
                                        .map(BambooPlan::getKey)
                                        .toList()));
    }

    private Map<String, String> fetchBuildStates(Map<String, List<String>> projectToKeys) {
        return projectToKeys.values().stream()
                .flatMap(List::stream)
                .distinct()
                .collect(
                        Collectors.toMap(
                                key -> key,
                                bambooClient::fetchBuildState));
    }

    private void printAndNotifyFailedBuilds(
            Map<String, List<String>> projectToKeys,
            Map<String, String> keyToBuildState) {
        keyToBuildState.entrySet().stream()
                .filter(entry -> !"Successful".equals(entry.getValue()))
                .forEach(entry -> {
                    String planKey = entry.getKey();
                    String buildState = entry.getValue();

                    // find project for key and show notification
                    projectToKeys.entrySet().stream()
                            .filter(e -> e.getValue().contains(planKey))
                            .findFirst()
                            .ifPresent(projectEntry -> {
                                String project = projectEntry.getKey();
                                trayService.showWarning(project, buildState);
                            });
                });
    }
}