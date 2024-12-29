package com.app.movie.trade.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public interface MovieDealCount {
	String getName();
	String getLanguage();
	LocalTime getDuration();
	String getCbfcrating();
	LocalDate getReleasedate();
	String getImageurl();
	String getFormattype();
	String getGenre();
	int getCount();
	int getMovieid();
}
