package com.app.movie.trade.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.movie.trade.entities.Transaction;
import com.app.movie.trade.exceptions.InvalidInvestmentStatusException;
import com.app.movie.trade.exceptions.InvalidTransactionStatusException;
import com.app.movie.trade.services.TransacationService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	@Autowired
	TransacationService transactionService;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction)
			throws InvalidTransactionStatusException, InvalidInvestmentStatusException {
		Transaction result = transactionService.createTransaction(transaction);
		return ResponseEntity.ok(result);
	}
}
