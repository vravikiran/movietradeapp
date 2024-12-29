package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.movie.trade.entities.BankAccountDetails;

public interface BankAccountDetailsRepository extends JpaRepository<BankAccountDetails, Integer> {

}
