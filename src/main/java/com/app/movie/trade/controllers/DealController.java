package com.app.movie.trade.controllers;

import java.time.LocalDate;
import java.util.List;

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

import com.app.movie.trade.entities.Deal;
import com.app.movie.trade.entities.DealCountRequest;
import com.app.movie.trade.entities.DealIdAndDealPrice;
import com.app.movie.trade.entities.DealRequestObj;
import com.app.movie.trade.entities.DealResult;
import com.app.movie.trade.entities.MovieDealCountPojo;
import com.app.movie.trade.entities.TheatreDeal;
import com.app.movie.trade.entities.TheatreDealRequest;
import com.app.movie.trade.services.DealService;

@RestController
@RequestMapping("/deal")
public class DealController {
	@Autowired
	DealService dealService;
	Logger logger = LoggerFactory.getLogger(DealController.class);

	@PostMapping
	public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {
		logger.info("Creation of deal started with details  :: " + deal.toString());
		Deal createdDeal = dealService.createDeal(deal);
		logger.info("Deal created successfully");
		return ResponseEntity.ok(createdDeal);
	}

	@GetMapping("/movie")
	public ResponseEntity<Page<DealResult>> getDealsByMovieAndDate(@RequestParam int movieid,
			@RequestParam(required = false) LocalDate date, @RequestParam int city_id,
			@RequestParam(defaultValue = "0") int page, @RequestParam int size) {
		logger.info("retrieving deals by movie id and date :: ");
		Pageable pageable = PageRequest.of(page, size);
		DealRequestObj dealRequestObj = new DealRequestObj(movieid, date, city_id);
		Page<DealResult> deals = dealService.getDealsByMovie(dealRequestObj, pageable);
		logger.info("completed fetching deals by movie and date");
		return ResponseEntity.ok(deals);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<MovieDealCountPojo>> getMovieDealCountByCity(@RequestParam int city_id,
			@RequestParam(required = false) String name, @RequestParam(required = false) String language,
			@RequestParam(defaultValue = "0") int page, @RequestParam int size) {
		DealCountRequest dealCountRequest = new DealCountRequest(language, city_id, name);
		logger.info("retrieving count of deals using date and city ");
		Pageable pageable = PageRequest.of(page, size);
		Page<MovieDealCountPojo> result = dealService.getMovieDealCountByCity(dealCountRequest, pageable);
		logger.info("completed fetching count of deals by date and city");
		return ResponseEntity.ok(result);
	}

	@GetMapping("/theatre")
	public ResponseEntity<Page<TheatreDeal>> getDealsByTheatreAndDate(@RequestParam int theatre_id,
			@RequestParam(required = false) LocalDate date, @RequestParam(defaultValue = "0") int page,
			@RequestParam int size) {
		logger.info("Fetching deals in a theatre and given date ::" + theatre_id + "," + date);
		Pageable pageable = PageRequest.of(page, size);
		TheatreDealRequest dealRequest = new TheatreDealRequest(theatre_id, date);
		Page<TheatreDeal> result = dealService.getDealsByTheatre(dealRequest, pageable);
		logger.info("Fetched list of deals in a given theatre and given date");
		return ResponseEntity.ok(result);
	}

	@GetMapping("/dates/movie")
	public ResponseEntity<List<LocalDate>> getDealDatesByMovie(@RequestParam int movieid) {
		logger.info("Getting dates of deals for given movie with id :: " + movieid);
		List<LocalDate> dates = dealService.getDealDatesByMovie(movieid);
		logger.info("Successfully fetched deal dates for a given movie :: " + movieid);
		return ResponseEntity.ok(dates);
	}

	@GetMapping("/dates/theatre")
	public ResponseEntity<List<LocalDate>> getDealDatesByTheatre(@RequestParam int theatre_id) {
		logger.info("Fetching dates of deals for given theatre with id :: " + theatre_id);
		List<LocalDate> dates = dealService.getDealDatesByTheatre(theatre_id);
		logger.info("Successfully fetched deal dates for a given theatre with id :: " + theatre_id);
		return ResponseEntity.ok(dates);
	}

	@PostMapping("/update/dealprice")
	public ResponseEntity<String> updateDealPrice(@RequestBody DealIdAndDealPrice dealPrice) {
		logger.info("Updating deal price for a deal with id ::"+dealPrice.getDealid());
		dealService.updateDealPrice(dealPrice.getDealid(), dealPrice.getTotal_dealprice());
		return ResponseEntity.ok("deal price updated successfully");
	}
}
