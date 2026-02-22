package org.romika.bamboonotifier.service;

import java.io.StringReader;
import java.util.List;

import org.romika.bamboonotifier.model.BambooPlan;
import org.romika.bamboonotifier.model.BambooPlansRoot;
import org.romika.bamboonotifier.model.BuildResult;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

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
			return List.of();
		}
		try {
			JAXBContext context = JAXBContext.newInstance(BambooPlansRoot.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return ((BambooPlansRoot) unmarshaller.unmarshal(new StringReader(xml))).getPlans().getPlanList();
		}
		catch (JAXBException e) {
			throw new IllegalArgumentException("Failed to unmarshal Bamboo plans", e);
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
		}
		catch (JAXBException e) {
			throw new IllegalArgumentException("Failed to unmarshal build result for key " + planKey, e);
		}
	}
}
