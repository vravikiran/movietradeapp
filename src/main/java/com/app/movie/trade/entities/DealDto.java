package com.app.movie.trade.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DealDto {
	private int dealid;
	private String theatreName;
	private String propertyName;
	private String location;
	private int houseCapacity;
	private double totalDealPrice;
	private double totalActualPrice;
	private double maxProfit;
	private List<CategoryPricing> pricingsByCategory;
	private String movieName;
	private String city;
	private int movieid;
	private LocalDate showDate;
	private LocalTime showTime;
	private int theatre_id;

	public LocalDate getShowDate() {
		return showDate;
	}

	public void setShowDate(LocalDate showDate) {
		this.showDate = showDate;
	}

	public LocalTime getShowTime() {
		return showTime;
	}

	public void setShowTime(LocalTime showTime) {
		this.showTime = showTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getDealid() {
		return dealid;
	}

	public void setDealid(int dealid) {
		this.dealid = dealid;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getHouseCapacity() {
		return houseCapacity;
	}

	public void setHouseCapacity(int houseCapacity) {
		this.houseCapacity = houseCapacity;
	}

	public double getTotalDealPrice() {
		return totalDealPrice;
	}

	public void setTotalDealPrice(double totalDealPrice) {
		this.totalDealPrice = totalDealPrice;
	}

	public double getTotalActualPrice() {
		return totalActualPrice;
	}

	public void setTotalActualPrice(double totalActualPrice) {
		this.totalActualPrice = totalActualPrice;
	}

	public double getMaxProfit() {
		return maxProfit;
	}

	public void setMaxProfit(double maxProfit) {
		this.maxProfit = maxProfit;
	}

	public List<CategoryPricing> getPricingsByCategory() {
		return pricingsByCategory;
	}

	public void setPricingsByCategory(List<CategoryPricing> pricingsByCategory) {
		this.pricingsByCategory = pricingsByCategory;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public int getMovieid() {
		return movieid;
	}

	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}

	public int getTheatre_id() {
		return theatre_id;
	}

	public void setTheatre_id(int theatre_id) {
		this.theatre_id = theatre_id;
	}
}
