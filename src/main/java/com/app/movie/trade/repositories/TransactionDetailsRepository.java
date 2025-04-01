package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.movie.trade.entities.TransactionDetails;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, String>{

}
