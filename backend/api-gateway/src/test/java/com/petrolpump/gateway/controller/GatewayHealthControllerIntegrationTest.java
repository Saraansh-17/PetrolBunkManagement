package com.petrolpump.gateway.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("gateway-test")
class GatewayHealthControllerIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void gatewayHealthReturnsOk() {
		webTestClient.get()
				.uri("/gateway/health")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType("application/json")
				.expectBody()
				.jsonPath("$.status").isEqualTo("UP")
				.jsonPath("$.service").isEqualTo("petrol-pump-api-gateway")
				.jsonPath("$.timestamp").exists();
	}
}
