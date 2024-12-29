package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.movie.trade.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select case when count(u) > 0 then true else false end from User u where lower(u.email)= lower(:email)")
	public boolean findByEmail(String email);

	@Query("select u from User u where lower(u.email)= lower(:email)")
	public User findUserDetailsByEmail(String email);
}
