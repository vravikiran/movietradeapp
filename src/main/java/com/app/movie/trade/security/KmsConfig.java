package com.app.movie.trade.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KmsConfig {
	private static final String key = "arn:aws:kms:ap-south-1:326113948308:key/f4155c4d-5787-4e67-98b2-a562ff2af649";

	public KmsConfig() {
		super();
	}

	public String getKey() {
		return key;
	}
}
