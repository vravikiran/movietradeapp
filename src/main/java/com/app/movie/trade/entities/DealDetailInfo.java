package com.app.movie.trade.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DealDetailInfo {
	 String getMoviename();
	 int getMovieid();
	 LocalDate getMoviereleasedate();
	 int getTheatreid();
	 String getTheatrename();
	 LocalDate getShowdate();
	 LocalTime getShowtime();
	 int getTotaldealprice();
	 int getCapacity();
}
