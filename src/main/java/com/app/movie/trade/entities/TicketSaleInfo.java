package com.app.movie.trade.entities;

import java.util.List;
import java.util.Objects;

public class TicketSaleInfo {
	private int dealid;
	private List<TicketSaleCategory> ticketSales;
	public int getDealid() {
		return dealid;
	}
	public void setDealid(int dealid) {
		this.dealid = dealid;
	}
	public List<TicketSaleCategory> getTicketSales() {
		return ticketSales;
	}
	public void setTicketSales(List<TicketSaleCategory> ticketSales) {
		this.ticketSales = ticketSales;
	}
	@Override
	public int hashCode() {
		return Objects.hash(dealid, ticketSales);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TicketSaleInfo other = (TicketSaleInfo) obj;
		return dealid == other.dealid && Objects.equals(ticketSales, other.ticketSales);
	}
	
}
