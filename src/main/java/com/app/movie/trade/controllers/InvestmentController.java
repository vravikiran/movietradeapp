package com.app.movie.trade.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.movie.trade.entities.Investment;
import com.app.movie.trade.entities.InvestmentRequest;
import com.app.movie.trade.entities.TicketSaleInfo;
import com.app.movie.trade.exceptions.InvalidInvestmentStatusException;
import com.app.movie.trade.exceptions.TransactionFailureException;
import com.app.movie.trade.services.InvestmentService;

@RestController
@RequestMapping("/investment")
public class InvestmentController {

	@Autowired
	InvestmentService investmentService;
	Logger logger = LoggerFactory.getLogger(InvestmentController.class);

	@PostMapping
	public ResponseEntity<Investment> createOrder(@RequestBody InvestmentRequest investmentRequest)
			throws TransactionFailureException {
		logger.info("started creating investment for deal with id :: "+investmentRequest.getDealid());
		Investment investment = investmentService.createInvestment(investmentRequest);
		logger.info("Investment created successfully for deal with id :: "+investmentRequest.getDealid());
		return ResponseEntity.ok().body(investment);
	}
	
	@PostMapping("/ticket/sales")
	public ResponseEntity<Investment> updateTicketsSoldForDeal(@RequestBody TicketSaleInfo ticketSaleInfo) {
		logger.info("Updating the tickets sold for a given deal with id :: "+ticketSaleInfo.getDealid());
		Investment investment = investmentService.updateTicketSales(ticketSaleInfo);
		logger.info("Successfully updated tickets sold for a deal with id ::"+ticketSaleInfo.getDealid());
		return ResponseEntity.ok(investment);
	}

	@GetMapping
	public ResponseEntity<Page<Investment>> getInvestmentsByStatus(@RequestParam(required = false) String status,@RequestParam long mobileno, @RequestParam(defaultValue = "0") int page, @RequestParam int size) throws InvalidInvestmentStatusException {
		logger.info("Fetching investments with status :: "+status);
		Pageable pageable = PageRequest.of(page, size);
		Page<Investment> investments = investmentService.getInvestmentsbyStaus(status,mobileno, pageable);
		logger.info("Successfully fetched investments with status :: "+status);
		return ResponseEntity.ok().body(investments);
	}
}
