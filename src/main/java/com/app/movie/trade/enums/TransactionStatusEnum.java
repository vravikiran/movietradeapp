package com.app.movie.trade.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TransactionStatusEnum {
	FAILED, SUCCESS;

	public static Map<String, TransactionStatusEnum> transactionStatusValues() {
		return Stream.of(values()).collect(Collectors.toMap(TransactionStatusEnum::name, Function.identity()));
	}
}
