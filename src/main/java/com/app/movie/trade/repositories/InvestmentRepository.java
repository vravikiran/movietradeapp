package com.app.movie.trade.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.movie.trade.entities.Investment;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, String> {
	@Query(value = "select inv from Investment inv where inv.mobileno = :mobileno and (:status IS NULL or inv.status=:status) order by inv.updated_date")
	public Page<Investment> getInvestmentsByStatus(@Param("status") String status,@Param("mobileno") long mobileno, Pageable pageable);

	@Query(value = "select inv from Investment inv where inv.dealid = :dealid")
	public Investment getInvDetailsByDealid(@Param("dealid") int dealid);
	
	@Query(value="select inv from Investment inv where inv.mobileno = :mobileno and inv.status in ('ONGOING','COMPLETED') order by inv.updated_date")
	public List<Investment> getActiveInvestments(@Param("mobileno") long mobileno);
}
