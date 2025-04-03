package com.app.movie.trade.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KmsConfig {
	@Value("${kmskey}")
	private String key;
	public KmsConfig() {
		super();
	}

	public String getKey() {
		return key;
	}
}
