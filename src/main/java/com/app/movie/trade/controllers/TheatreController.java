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

import com.app.movie.trade.entities.Theatre;
import com.app.movie.trade.services.TheatreService;

@RestController
@RequestMapping("/theatre")
public class TheatreController {
	@Autowired
	TheatreService theatreService;
	Logger logger = LoggerFactory.getLogger(TheatreController.class);

	@PostMapping
	public ResponseEntity<Theatre> createTheatre(@RequestBody Theatre theatre) {
		logger.info("Started creating theatre with given details :: "+theatre.toString());
		Theatre createdTheatre = theatreService.createTheatre(theatre);
		logger.info("Theatre created successfully");
		return ResponseEntity.ok(createdTheatre);
	}
	
	@GetMapping
	public ResponseEntity<Page<Theatre>> getTheatresByCity(@RequestParam int city_id,@RequestParam(defaultValue = "0") int page, @RequestParam int size) {
		logger.info("Fetching theatres in given city :: "+city_id);
		Pageable pageable = PageRequest.of(page, size);
		Page<Theatre> theatres = theatreService.getTheatresbyCity(city_id, pageable);
		logger.info("Successfully fetched theatres in city with id :: "+city_id);
		return ResponseEntity.ok().body(theatres);
	}
}
