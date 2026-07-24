package org.romika.bamboonotifier.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.romika.bamboonotifier.model.BambooPlan;
import org.romika.bamboonotifier.model.BambooPlansRoot;
import org.romika.bamboonotifier.model.BuildResult;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.StringReader;
import java.util.List;

@Slf4j
@Service
public class BambooClient {
    private final WebClient webClient;

    public BambooClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<BambooPlan> fetchPlans(int maxPlans) {
        String xml = webClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/rest/api/latest/plan")
                                .queryParam("max-results", maxPlans)
                                .build())
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (xml == null) {
            log.error("Could not fetch plans from Bamboo");
            return List.of();
        }
        try {
            JAXBContext context = JAXBContext.newInstance(BambooPlansRoot.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return ((BambooPlansRoot) unmarshaller.unmarshal(new StringReader(xml))).getPlans().getPlanList();
        } catch (JAXBException e) {
            log.error("Could not parse Bamboo plans", e);
            return List.of();
        }
    }

    public String fetchBuildState(String planKey) {
        String xml = webClient
                .get()
                .uri("/rest/api/latest/result/{key}/latest", planKey)
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (xml == null) {
            return null;
        }
        try {
            JAXBContext context = JAXBContext.newInstance(BuildResult.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            BuildResult result = (BuildResult) unmarshaller.unmarshal(new StringReader(xml));
            return result.getBuildState();
        } catch (JAXBException e) {
            log.error("Could not parse build state for key: " + planKey, e);
            throw new IllegalArgumentException("Failed to unmarshal build result for key " + planKey, e);
        }
    }
}
