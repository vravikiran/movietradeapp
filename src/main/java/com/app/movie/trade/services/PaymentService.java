package com.app.movie.trade.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	Logger logger = LoggerFactory.getLogger(PaymentService.class);
	public boolean makePayment(double amount) {
		return true;
	}
}
