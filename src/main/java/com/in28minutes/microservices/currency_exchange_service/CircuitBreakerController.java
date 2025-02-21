package com.in28minutes.microservices.currency_exchange_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

	@GetMapping("/sample-api")
	//@Retry(name = "sample-api", fallbackMethod = "hardcodedResponse")
	@CircuitBreaker(name = "sample-api", fallbackMethod = "hardcodedResponse")
	public String sampleApi() {
		logger.info("Sample api call received");
		ResponseEntity<String> forEntity = new RestTemplate()
				.getForEntity("http://localhost:8080/some-dummy-url",
						String.class);
		return forEntity.getBody();
	}
	
	public String hardcodedResponse(Exception e) {
		return "fallback-response";
	}
	
	@GetMapping("/sample-api-ratelimiter")
	@RateLimiter(name = "sample-api-ratelimiter")
	public String rateLimiterApi() {
		logger.info("sample rate limiter response");
		
		return "sample rate limiter response";
	}
	
	@GetMapping("/sample-api-bulkhead")
	@Bulkhead(name = "sample-api-bulkhead")
	public String bulkHeadApi() {
		logger.info("sample bulkhead response");
		
		return "sample bulkhead response";
	}
}
