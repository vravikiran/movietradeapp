package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.movie.trade.entities.SeatCategoryCapacity;

@Repository
public interface SeatCategoryCapacityRepository extends JpaRepository<SeatCategoryCapacity, Integer> {

}
