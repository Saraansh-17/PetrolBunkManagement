package com.petrolpump.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Logs each proxied request: HTTP method, path, response status, and elapsed time.
 */
@Component
public class RequestLoggingGlobalFilter implements GlobalFilter, Ordered {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingGlobalFilter.class);

	static final String START_TIME_NS = RequestLoggingGlobalFilter.class.getName() + ".startTimeNs";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		exchange.getAttributes().put(START_TIME_NS, System.nanoTime());

		String method = exchange.getRequest().getMethod().name();
		String path = exchange.getRequest().getURI().getPath();

		return chain.filter(exchange)
				.doFinally(signal -> {
					Long startNs = exchange.getAttribute(START_TIME_NS);
					long elapsedMs = startNs == null ? -1L : (System.nanoTime() - startNs) / 1_000_000L;
					HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
					int statusValue = statusCode != null ? statusCode.value() : 0;

					log.info("{} {} -> {} ({} ms)", method, path, statusValue, elapsedMs);
				});
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
