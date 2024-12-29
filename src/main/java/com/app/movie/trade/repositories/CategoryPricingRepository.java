package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.movie.trade.entities.CategoryPricing;

@Repository
public interface CategoryPricingRepository extends JpaRepository<CategoryPricing, Integer> {

}
