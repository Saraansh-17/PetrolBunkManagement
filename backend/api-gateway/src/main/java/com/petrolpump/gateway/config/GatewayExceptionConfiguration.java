package com.petrolpump.gateway.config;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import com.petrolpump.gateway.exception.GlobalErrorWebExceptionHandler;

/**
 * Registers a unified JSON {@link ErrorWebExceptionHandler} for the reactive stack.
 */
@Configuration
public class GatewayExceptionConfiguration {

	@Bean
	@Order(-2)
	public ErrorWebExceptionHandler globalErrorWebExceptionHandler(
			ErrorAttributes errorAttributes,
			WebProperties webProperties,
			ServerProperties serverProperties,
			ObjectProvider<List<ViewResolver>> viewResolversProvider,
			ServerCodecConfigurer serverCodecConfigurer,
			ApplicationContext applicationContext) {

		GlobalErrorWebExceptionHandler handler = new GlobalErrorWebExceptionHandler(
				errorAttributes,
				webProperties,
				serverProperties,
				applicationContext);
		handler.setMessageReaders(serverCodecConfigurer.getReaders());
		handler.setMessageWriters(serverCodecConfigurer.getWriters());
		handler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
		return handler;
	}
}
