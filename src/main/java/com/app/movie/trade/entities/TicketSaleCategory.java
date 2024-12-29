package com.app.movie.trade.entities;

public class TicketSaleCategory {
	int categoryId;
	int numberOfTickets;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public TicketSaleCategory(int categoryId, int numberOfTickets) {
		super();
		this.categoryId = categoryId;
		this.numberOfTickets = numberOfTickets;
	}

	public TicketSaleCategory() {
		super();
	}

}
