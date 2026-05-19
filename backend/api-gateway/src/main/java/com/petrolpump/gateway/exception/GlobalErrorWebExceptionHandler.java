package com.petrolpump.gateway.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/**
 * JSON error payloads for WebFlux failures (upstream errors, bad requests, etc.).
 */
public class GlobalErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	public GlobalErrorWebExceptionHandler(
			ErrorAttributes errorAttributes,
			WebProperties webProperties,
			ServerProperties serverProperties,
			ApplicationContext applicationContext) {
		super(errorAttributes, webProperties.getResources(), serverProperties.getError(),
				applicationContext);
	}

	@Override
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Map<String, Object> attrs = getErrorAttributes(request,
				getErrorAttributeOptions(request, MediaType.APPLICATION_JSON));
		Throwable error = getError(request);
		int statusCode = resolveHttpStatus(attrs, error);
		HttpStatus resolved = HttpStatus.resolve(statusCode);
		String reason = resolved != null ? resolved.getReasonPhrase() : "Error";

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", attrs.getOrDefault("timestamp", ""));
		body.put("status", statusCode);
		body.put("error", reason);
		body.put("message", attrs.getOrDefault("message", ""));
		body.put("path", attrs.getOrDefault("path", request.path()));

		return ServerResponse.status(statusCode)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body));
	}

	private static int resolveHttpStatus(Map<String, Object> attrs, Throwable error) {
		Object statusAttr = attrs.get("status");
		if (statusAttr instanceof Integer i) {
			return i;
		}
		if (error instanceof ResponseStatusException rse) {
			return rse.getStatusCode().value();
		}
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}
}
