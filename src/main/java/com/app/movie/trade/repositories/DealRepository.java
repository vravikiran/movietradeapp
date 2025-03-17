package com.app.movie.trade.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.movie.trade.entities.Deal;
import com.app.movie.trade.entities.DealDetailInfo;
import com.app.movie.trade.entities.MovieDealCount;

@Repository
@RedisHash
public interface DealRepository extends JpaRepository<Deal, Integer> {
	@Query(value = "select d as deal from Deal d where d.city_id = :city_id and d.showdate =:showdate and d.movieid = :movieid")
	public List<Deal> getDealsByMovieId(int movieid, LocalDate showdate, int city_id);

	@Query(value = "select  m.name as name,m.language as language,m.duration as duration,m.cbfc_rating as cbfcrating,m.release_date as releasedate,m.imageurl as imageurl,m.format_type as formattype,m.genre as genre, count(*) as count, d.movieid as movieid from Deal d ,Movie m  where d.is_invested = false and d.city_id = :city_id and d.movieid = m.movieid and (:language IS NULL or m.language = :language) and (:name IS NULL or m.name like %:name% ) group by d.movieid order by m.release_date")
	public List<MovieDealCount> getMovieDealCountByCity(@Param("city_id") int city_id, @Param("name") String name,
			@Param("language") String language);

	@Query(value = "select distinct(d.showdate) from Deal d where d.movieid = :movieid order by showdate asc")
	public List<LocalDate> getDealDatesByMovie(@RequestParam int movieid);

	@Query(value = "select m.name as moviename,m.movieid as movieid,m.release_date as moviereleasedate,t.theatre_id  as theatreid,t.theatre_name as theatrename,t.capacity as capacity, d.showdate as showdate,d.showtime as showtime,d.total_dealprice as totaldealprice from Deal  d, Movie m , Theatre t where d.dealid = :dealid and d.theatre_id = t.theatre_id and d.movieid = m.movieid")
	public DealDetailInfo getDealDetailedInfo(@Param("dealid") int dealid);

	@Query(value="select d from Deal d, Theatre t  where d.showdate =:date and d.theatre_id = :theatre_id  and d.theatre_id =t.theatre_id")
	public List<Deal> getDealsByTheatreAndDate(@Param("theatre_id") int theatre_id,@Param("date") LocalDate date);
	
	@Query(value ="select distinct(d.showdate) from Deal d WHERE d.is_invested = false and d.theatre_id = :theatre_id  order by d.showdate ASC")
	public List<LocalDate> getDealDatesByTheatre(@Param("theatre_id") int theatre_id);
	
}
