package com.app.movie.trade.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.movie.trade.entities.PaymentResponse;
import com.app.movie.trade.entities.Transaction;
import com.app.movie.trade.entities.TransactionStatus;
import com.app.movie.trade.exceptions.InvalidInvestmentStatusException;
import com.app.movie.trade.exceptions.InvalidTransactionStatusException;
import com.app.movie.trade.services.TransacationService;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	Logger logger = LoggerFactory.getLogger(TransactionController.class);
	@Autowired
	TransacationService transactionService;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction)
			throws InvalidTransactionStatusException, InvalidInvestmentStatusException {
		Transaction result = transactionService.createTransaction(transaction);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/details/update")
	public ResponseEntity<String> saveTransactionDetails(HttpServletRequest request, @RequestBody String response)
			throws IOException {
		logger.info("Response received from phonepe");
		Gson gson = new Gson();
		PaymentResponse paymentResponse = gson.fromJson(response, PaymentResponse.class);
		logger.info("updating transaction details started");
		boolean isVerified = false;
		String xVerifyHeader = request.getHeader("X-VERIFY");
		logger.info("xVerifyHeader ::" + xVerifyHeader);
		if (xVerifyHeader != null) {
			isVerified = transactionService.verifyXVerifyHeader(xVerifyHeader, response);
		}
		if (!isVerified) {
			logger.info("X-VERIFY header missing/unverified transaction");
			return ResponseEntity.badRequest().body("X-VERIFY header missing/unverified transaction");
		} else {
			logger.info("Saving the transaction details");
			transactionService.savePaymentDetails(paymentResponse);
		}
		logger.info("updating transaction details completed");
		return ResponseEntity.ok().build();
	}

	@PostMapping("/update")
	public ResponseEntity<HttpStatus> updateTransactionStatus(@RequestBody TransactionStatus transactionStatus) {
		transactionService.updateTransactionStatus(transactionStatus);
		return ResponseEntity.ok().build();
	}
}
