package com.app.movie.trade.services;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.Investment;
import com.app.movie.trade.entities.Transaction;
import com.app.movie.trade.enums.InvestmentStatusEnum;
import com.app.movie.trade.enums.TransactionStatusEnum;
import com.app.movie.trade.exceptions.InvalidInvestmentStatusException;
import com.app.movie.trade.exceptions.InvalidTransactionStatusException;
import com.app.movie.trade.repositories.InvestmentRepository;
import com.app.movie.trade.repositories.TransactionRepository;

@Service
public class TransacationService {
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	InvestmentRepository investmentRepository;
	@Autowired
	InvestmentService investmentService;
	@Autowired
	DealService dealService;

	public Transaction createTransaction(Transaction transaction)
			throws InvalidTransactionStatusException, InvalidInvestmentStatusException {
		if(transaction.getInvestment_id() == null) {
			throw new NoSuchElementException("investment id cannot be null");
		}
		if (investmentRepository.existsById(transaction.getInvestment_id().toUpperCase())) {
			if (TransactionStatusEnum.valueOf(transaction.getStatus()) != null) {
				Investment investment = investmentRepository.getReferenceById(transaction.getInvestment_id().toUpperCase());
				transaction.setTransaction_date(LocalDate.now());
				investment.setStatus(InvestmentStatusEnum.ONGOING.name());
				investment.setTransaction(transaction);
				dealService.updateDealStatus(true, investment.getDealid());
				return transaction;
			} else {
				throw new InvalidTransactionStatusException("Invalid transaction status");
			}

		} else {
			throw new NoSuchElementException("no investment found with given id:" + transaction.getInvestment_id());
		}
	}
}
