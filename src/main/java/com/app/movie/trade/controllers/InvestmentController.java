package com.app.movie.trade.controllers;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.app.movie.trade.services.InvestmentService;

@RestController
@RequestMapping("/investment")
public class InvestmentController {

	@Autowired
	InvestmentService investmentService;
	Logger logger = LoggerFactory.getLogger(InvestmentController.class);

	@PostMapping
	public ResponseEntity<Investment> createOrder(@RequestBody InvestmentRequest investmentRequest)
			throws NoSuchElementException {
		logger.info("started creating investment for deal with id :: " + investmentRequest.getDealid());
		Investment investment = investmentService.createInvestment(investmentRequest);
		logger.info("Investment created successfully for deal with id :: " + investmentRequest.getDealid());
		return ResponseEntity.ok().body(investment);
	}

	@PostMapping("/ticket/sales")
	public ResponseEntity<Investment> updateTicketsSoldForDeal(@RequestBody TicketSaleInfo ticketSaleInfo) {
		logger.info("Updating the tickets sold for a given deal with id :: " + ticketSaleInfo.getDealid());
		Investment investment = investmentService.updateTicketSales(ticketSaleInfo);
		logger.info("Successfully updated tickets sold for a deal with id ::" + ticketSaleInfo.getDealid());
		return ResponseEntity.ok(investment);
	}

	@GetMapping
	public ResponseEntity<Page<Investment>> getInvestmentsByStatus(@RequestParam(required = false) String status,
			@RequestParam long mobileno, @RequestParam(defaultValue = "0") int page, @RequestParam int size)
			throws InvalidInvestmentStatusException {
		logger.info("Fetching investments with status :: " + status);
		Pageable pageable = PageRequest.of(page, size);
		Page<Investment> investments = investmentService.getInvestmentsbyStaus(status, mobileno, pageable);
		logger.info("Successfully fetched investments with status :: " + status);
		return ResponseEntity.ok().body(investments);
	}

	@PostMapping("/update/status")
	public ResponseEntity<String> updateInvestmentStatus(@RequestParam String investment_id,
			@RequestBody Map<String, String> valuesToUpdate)
			throws NoSuchElementException, InvalidInvestmentStatusException {
		logger.info("Updating status for invesetment with id :: " + investment_id);
		investmentService.updateInvestmentStatus(investment_id, valuesToUpdate);
		return ResponseEntity.ok("Investment status updated successfully");
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteInvestmentById(@RequestParam String investment_id) {
		investmentService.deleteInvestmentById(investment_id);
		return ResponseEntity.ok("Investment deleted successfully");
	}

	@GetMapping("/all")
	ResponseEntity<List<Investment>> getAllInvestments(@RequestParam long mobileno) {
		List<Investment> investments = investmentService.getAllInvestments(mobileno);
		return ResponseEntity.ok(investments);
	}

	@GetMapping("/verify")
	public ResponseEntity<String> verifyInvForDeal(@RequestParam int dealId) {
		logger.info("Verifying if any investment exists for given dealId :: "+dealId);
		boolean isExists = investmentService.verifyInvestmentForDeal(dealId);
		if (isExists) {
			logger.info("investment exists for given dealId :: "+dealId);
			return ResponseEntity.status(211).body("Deal is already booked");
		} else {
			logger.info("No investment exists for given dealId :: "+dealId);
			return ResponseEntity.status(212).body("No investment exists for given deal id");
		}
	}
}
