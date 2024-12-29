package com.app.movie.trade.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.movie.trade.entities.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Integer> {
	@Query(value = "select t from Theatre t where t.city_id= :city_id")
	public Page<Theatre> getTheatresByCity(@Param("city_id") int city_id, Pageable pageable);
}
