package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.movie.trade.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{

}
