package com.app.movie.trade.helpers;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class LocalTimeTest {
	public static void main(String args[]) {
		System.out.println(LocalTime.now() .truncatedTo(ChronoUnit.MINUTES));
	}
}
