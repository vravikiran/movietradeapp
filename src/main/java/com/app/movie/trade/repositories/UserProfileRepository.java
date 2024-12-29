package com.app.movie.trade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.movie.trade.entities.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	@Query("select case when count(u) > 0 then true else false end from UserProfile u where upper(u.email)= upper(:email)")
	public boolean existsByEmail(String email);
	@Query("select case when count(u) > 0 then true else false end from UserProfile u where upper(u.pan_number)= upper(:panno)")
	public boolean existsByPanNumber(String panno);
}
