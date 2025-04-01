package com.app.movie.trade.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.City;
import com.app.movie.trade.repositories.CityRepository;

@Service
public class CityService {
	@Autowired
	CityRepository cityRepository;
	Logger logger = LoggerFactory.getLogger(CityService.class);

	public List<City> getListOfCities() {
		logger.info("Fetching list of cities");
		return cityRepository.findAll();
	}
	
	public City createCity(City city) {
		if(cityRepository.verifyCityByName(city.getCity_name()))
		return cityRepository.save(city);
		else 
			return null;
	}
}
