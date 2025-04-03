package com.app.movie.trade.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhonepeConfig {
	@Value("${phonepe.merchant.id}")
	private String phonePeMerchantId;
	@Value("${phonepe.salt.index}")
	private String phonePeSaltIndex;
	@Value("${phonepe.trans.key}")
	private String phonePeSaltKey;

	public String getPhonePeSaltIndex() {
		return phonePeSaltIndex;
	}

	public String getPhonePeSaltKey() {
		return phonePeSaltKey;
	}

	public String getPhonePeMerchantId() {
		return phonePeMerchantId;
	}
}
