package com.app.movie.trade.helpers;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LongStringConverter implements AttributeConverter<Long, String> {
	@Autowired
	KmsUtil kmsUtil;

	@Override
	public String convertToDatabaseColumn(Long attribute) {
	String value =	Long.toString(attribute.longValue());
	System.out.println(value);
		return kmsUtil.kmsEncrypt(value);
	}

	@Override
	public Long convertToEntityAttribute(String dbData) {
		return Long.valueOf(kmsUtil.kmsDecrypt(dbData));
	}

}
