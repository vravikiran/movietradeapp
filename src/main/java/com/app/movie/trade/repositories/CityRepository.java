package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.movie.trade.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
	@Query("select case when count(c) > 0 then true else false end from City c where upper(c.city_name)= upper(:city)")
	public boolean verifyCityByName(String city);
}
