package com.app.movie.trade.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.movie.trade.entities.City;
import com.app.movie.trade.services.CityService;

@RestController
@RequestMapping("/cities")
public class CityController {
	@Autowired
	CityService cityService;
	Logger logger = LoggerFactory.getLogger(CityController.class);

	@GetMapping
	public ResponseEntity<List<City>> getListOfCities() {
		logger.info("Fetching the list of cities :: ");
		List<City> citiesList = cityService.getListOfCities();
		logger.info("Successfully fetched the list of cities");
		return ResponseEntity.ok(citiesList);
	}
}