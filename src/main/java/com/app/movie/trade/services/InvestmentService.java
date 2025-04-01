package com.app.movie.trade.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.DealDetailInfo;
import com.app.movie.trade.entities.Investment;
import com.app.movie.trade.entities.InvestmentRequest;
import com.app.movie.trade.entities.TicketSaleInfo;
import com.app.movie.trade.entities.Transaction;
import com.app.movie.trade.enums.InvestmentStatusEnum;
import com.app.movie.trade.exceptions.InvalidInvestmentStatusException;
import com.app.movie.trade.repositories.DealRepository;
import com.app.movie.trade.repositories.InvestmentRepository;
import com.app.movie.trade.repositories.TransactionRepository;

@Service
public class InvestmentService {
	@Autowired
	DealRepository dealRepository;
	@Autowired
	InvestmentRepository investmentRepository;
	@Autowired
	DealService dealService;
	@Autowired
	PaymentService paymentService;
	@Autowired
	TransactionRepository transactionRepository;
	Logger logger = LoggerFactory.getLogger(InvestmentService.class);

	public Investment createInvestment(InvestmentRequest investmentRequest) {
		logger.info("Creation of investment started for deal with id  ::" + investmentRequest.getDealid());
		Investment investment = null;
		if (dealRepository.existsById(investmentRequest.getDealid())) {
			investment = new Investment();
			DealDetailInfo dealDetailInfo = dealRepository.getDealDetailedInfo(investmentRequest.getDealid());
			investment.setDealid(investmentRequest.getDealid());
			investment.setMovie_name(dealDetailInfo.getMoviename());
			investment.setMovie_release_date(dealDetailInfo.getMoviereleasedate());
			investment.setMovieid(dealDetailInfo.getMovieid());
			investment.setShowdate(dealDetailInfo.getShowdate());
			investment.setShowtime(dealDetailInfo.getShowtime());
			investment.setTheatre_id(dealDetailInfo.getTheatreid());
			investment.setTheatre_name(dealDetailInfo.getTheatrename());
			investment.setInvestedamt(dealDetailInfo.getTotaldealprice());
			investment.setStatus(InvestmentStatusEnum.PROCESSING.toString());
			investment.setInvestment_id(
					"MT" + String.valueOf(investmentRequest.getDealid()) + "-" + LocalDateTime.now().toString());
			investment.setHouse_capacity(dealDetailInfo.getCapacity());
			investment.setCreated_date(LocalDate.now());
			investment.setUpdated_date(LocalDate.now());
			investment.setMobileno(investmentRequest.getMobileno());
			investment.setTransaction_id(investmentRequest.getTransaction_id());
			Transaction transaction = new Transaction();
			transaction.setTransaction_id(investmentRequest.getTransaction_id());
			transaction.setInvestment_id(investment.getInvestment_id());
			transaction.setTransaction_date(LocalDate.now());
			transaction.setAmount(investmentRequest.getAmounttopay());
			transactionRepository.save(transaction);
			investmentRepository.save(investment);
			logger.info("Investment created successfully for deal with id :: " + investment.getDealid());
			return investment;
		} else {
			throw new NoSuchElementException("Deal with given ID doesn't exists :: " + investmentRequest.getDealid());
		}
	}

	public Page<Investment> getInvestmentsbyStaus(String status, long mobileno, Pageable pageable)
			throws InvalidInvestmentStatusException {
		logger.info("Fetching investments based on status :: " + status);
		if (status != null && !InvestmentStatusEnum.invStatusValues().containsKey(status.toUpperCase())) {
			logger.info("Invalid investment status");
			throw new InvalidInvestmentStatusException("not a valid Investment Status");
		} else {
			List<String> statuses = null;
			if (status != null && status.equalsIgnoreCase(InvestmentStatusEnum.ONGOING.name())) {
				statuses = new ArrayList<>();
				statuses.add(InvestmentStatusEnum.ONGOING.name());
				statuses.add(InvestmentStatusEnum.PROCESSING.name());
			} else if (status != null && status.equalsIgnoreCase(InvestmentStatusEnum.COMPLETED.name())) {
				statuses = new ArrayList<>();
				statuses.add(status);
			}
			return investmentRepository.getInvestmentsByStatus(statuses, mobileno, pageable);
		}
	}

	public Investment updateTicketSales(TicketSaleInfo ticketSaleInfo) {
		logger.info("updating ticket sales for deal with id :: " + ticketSaleInfo.getDealid());
		ticketSaleInfo.getDealid();
		Investment investment = investmentRepository.getInvDetailsByDealid(ticketSaleInfo.getDealid());
		Investment updatedInvestment = investmentRepository.save(investment);
		logger.info("succesfully updated ticket sales for a deal with id :: " + ticketSaleInfo.getDealid());
		return updatedInvestment;
	}

	@Async
	private void updateDealStatus(int dealid, boolean isactive) {
		logger.info("Successfully updated deal status for deal with id :: " + dealid + " , " + isactive);
		dealService.updateDealStatus(isactive, dealid);
	}

	public void updateInvestmentStatus(String investment_id, Map<String, String> valuesToUpdate)
			throws InvalidInvestmentStatusException {
		logger.info("Updating status for invesetment with id :: " + investment_id);
		String status = valuesToUpdate.get("status");
		if (investmentRepository.existsById(investment_id.toUpperCase())) {
			if (status != null && InvestmentStatusEnum.invStatusValues().containsKey(status.toUpperCase())) {
				Investment investment = investmentRepository.getReferenceById(investment_id.toUpperCase());
				investment.setStatus(status.toUpperCase());
				investment.setUpdated_date(LocalDate.now());
				investmentRepository.save(investment);
			} else {
				logger.info("Invalid investment status");
				throw new InvalidInvestmentStatusException("not a valid Investment Status");
			}
		} else {
			logger.info("No investment found with given id :: " + investment_id);
			throw new NoSuchElementException("No investment found with given id :: " + investment_id);
		}
	}

	public void deleteInvestmentById(String investment_id) {
		if (investmentRepository.existsById(investment_id)) {
			Investment investment = investmentRepository.getReferenceById(investment_id);
			investment.setStatus(InvestmentStatusEnum.CANCELLED.name());
			updateDealStatus(investment.getDealid(), false);
			investment.setUpdated_date(LocalDate.now());
		} else {
			logger.info("No investment found with given id :: " + investment_id);
			throw new NoSuchElementException("No investment found with given id :: " + investment_id);
		}
	}

	public boolean verifyInvestmentForDeal(int dealId) {
		boolean isInvExistsForDeal = dealRepository.isInvExistsForDeal(dealId);
		return isInvExistsForDeal && investmentRepository.isInvExistsForDeal(dealId);
	}

	public List<Investment> getAllInvestments(long mobileno) {
		return investmentRepository.getActiveInvestments(mobileno);
	}
}
