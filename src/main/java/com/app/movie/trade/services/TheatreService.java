package com.app.movie.trade.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.SeatCategoryCapacity;
import com.app.movie.trade.entities.Theatre;
import com.app.movie.trade.repositories.TheatreRepository;

@Service
public class TheatreService {
	@Autowired
	TheatreRepository theatreRepository;
	Logger logger = LoggerFactory.getLogger(TheatreService.class);

	public Theatre createTheatre(Theatre theatre) {
		logger.info("Started creating theatre with details :: "+theatre.toString());
		List<SeatCategoryCapacity> seatCategoryCapacities = theatre.getSeatCategoryCapacities();
		for(SeatCategoryCapacity seatCategoryCapacity:seatCategoryCapacities) {
			seatCategoryCapacity.setTheatre(theatre);
		}
		theatre.setSeatCategoryCapacities(seatCategoryCapacities);
		Theatre createdTheatre = theatreRepository.save(theatre);
		logger.info("Theatre created successfully");
		return createdTheatre;
	}
	
	public Page<Theatre> getTheatresbyCity(int city_id,Pageable pageable) {
		logger.info("Fetching theatres in a given city :: "+city_id);
		return theatreRepository.getTheatresByCity(city_id, pageable);
	}
}
