package com.petrolpump.gateway.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * In-process health surface for the gateway (not proxied to downstream services).
 */
@RestController
@RequestMapping("/gateway")
public class GatewayHealthController {

	@GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> health() {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", "UP");
		body.put("service", "petrol-pump-api-gateway");
		body.put("timestamp", Instant.now().toString());
		return ResponseEntity.ok(body);
	}
}
