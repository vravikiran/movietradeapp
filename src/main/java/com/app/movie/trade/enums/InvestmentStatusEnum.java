package com.app.movie.trade.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum InvestmentStatusEnum {
	ONGOING,COMPLETED;
	public static Map<String,InvestmentStatusEnum> invStatusValues() {
		return Stream.of(values()).collect(Collectors.toMap(InvestmentStatusEnum::name,Function.identity()));
	}
}
