package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.movie.trade.entities.ValidUserAccount;

public interface ValidUserAccountRepository extends JpaRepository<ValidUserAccount, Long> {

}
