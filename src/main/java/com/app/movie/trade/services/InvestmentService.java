package com.app.movie.trade.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.CategoryPricing;
import com.app.movie.trade.entities.Deal;
import com.app.movie.trade.entities.DealDetailInfo;
import com.app.movie.trade.entities.Investment;
import com.app.movie.trade.entities.InvestmentRequest;
import com.app.movie.trade.entities.TicketSaleCategory;
import com.app.movie.trade.entities.TicketSaleInfo;
import com.app.movie.trade.enums.InvestmentStatusEnum;
import com.app.movie.trade.exceptions.InvalidInvestmentStatusException;
import com.app.movie.trade.exceptions.TransactionFailureException;
import com.app.movie.trade.repositories.DealRepository;
import com.app.movie.trade.repositories.InvestmentRepository;

@Service
public class InvestmentService {
	@Autowired
	DealRepository dealRepository;
	@Autowired
	InvestmentRepository investmentRepository;
	@Autowired
	DealService dealService;
	Logger logger = LoggerFactory.getLogger(InvestmentService.class);

	public Investment createInvestment(InvestmentRequest investmentRequest) throws TransactionFailureException {
		logger.info("Creation of investment started for deal with id  ::"+investmentRequest.getDealid());
		Investment investment = null;
		boolean isPaymentSuccess = true;
		if (isPaymentSuccess) {
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
			investment.setStatus(InvestmentStatusEnum.ONGOING.toString());
			investment.setInvestment_id("MT" + String.valueOf(investmentRequest.getDealid()));
			investment.setHouse_capacity(dealDetailInfo.getCapacity());
			investment.setCreated_date(LocalDate.now());
			investment.setUpdated_date(LocalDate.now());
			investment.setMobileno(investmentRequest.getMobileno());
			updateDealStatus(investmentRequest.getDealid(), isPaymentSuccess);
			investmentRepository.save(investment);
			logger.info("Investment created successfully for deal with id :: "+investment.getDealid());
			return investment;
		} else {
			throw new TransactionFailureException("transaction failed. Please retry payment");
		}
	}

	public Page<Investment> getInvestmentsbyStaus(String status,long mobileno, Pageable pageable)
			throws InvalidInvestmentStatusException {
		logger.info("Fetching investments based on status :: "+status);
		if (status != null && !InvestmentStatusEnum.invStatusValues().containsKey(status.toUpperCase())) {
			logger.info("Invalid investment status");
			throw new InvalidInvestmentStatusException("not a valid Investment Status");
		} else {
			return investmentRepository.getInvestmentsByStatus(status,mobileno, pageable);
		}
	}
	
	public Investment updateTicketSales(TicketSaleInfo ticketSaleInfo) {
		logger.info("updating ticket sales for deal with id :: "+ticketSaleInfo.getDealid());
		ticketSaleInfo.getDealid();
		Deal deal =  dealRepository.getReferenceById(ticketSaleInfo.getDealid());
		List<CategoryPricing> categoryPricings =  deal.getPricesByCategory();
		Investment investment = investmentRepository.getInvDetailsByDealid(ticketSaleInfo.getDealid());
		List<TicketSaleCategory> saleCategories = ticketSaleInfo.getTicketSales().stream().sorted(Comparator.comparing(TicketSaleCategory::getCategoryId)).toList();
		double earnedAmount = 0.0;
		int ticketsSold = 0;
		for(TicketSaleCategory ticketSaleCategory:saleCategories) {
			CategoryPricing  categoryPricing = categoryPricings.stream().filter(a->a.getCategory_id()==ticketSaleCategory.getCategoryId()).findFirst().get();
			earnedAmount+= ticketSaleCategory.getNumberOfTickets()*(categoryPricing.getDeal_ticketprice());
			ticketsSold+=ticketSaleCategory.getNumberOfTickets();
		}
		investment.setTickets_sold(ticketsSold);
		investment.setEarnedamt(earnedAmount);
		Investment updatedInvestment = investmentRepository.save(investment);
		logger.info("succesfully updated ticket sales for a deal with id :: "+ticketSaleInfo.getDealid());
		return updatedInvestment;
	}

	@Async
	private void updateDealStatus(int dealid, boolean isactive) {
		logger.info("Successfully updated deal status for deal with id :: "+dealid+" , "+isactive);
		dealService.updateDealStatus(isactive, dealid);
	}
}
