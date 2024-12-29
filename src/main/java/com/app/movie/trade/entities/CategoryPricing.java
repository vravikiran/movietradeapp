package com.app.movie.trade.entities;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "category_pricing")
public class CategoryPricing implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private double actual_ticketprice;
	private double deal_ticketprice;
	private String category_name;
	@ManyToOne
	@JoinColumn(name = "dealid")
	@JsonIgnore
	private Deal deal;

	private int category_id;

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getActual_ticketprice() {
		return actual_ticketprice;
	}

	public void setActual_ticketprice(double actual_ticketprice) {
		this.actual_ticketprice = actual_ticketprice;
	}

	public double getDeal_ticketprice() {
		return deal_ticketprice;
	}

	public void setDeal_ticketprice(double deal_ticketprice) {
		this.deal_ticketprice = deal_ticketprice;
	}

	public String getCategory_name() {
		return category_name;
	}

	public Deal getDeal() {
		return deal;
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actual_ticketprice, category_name, deal, deal_ticketprice, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryPricing other = (CategoryPricing) obj;
		return Double.doubleToLongBits(actual_ticketprice) == Double.doubleToLongBits(other.actual_ticketprice)
				&& Objects.equals(category_name, other.category_name) && Objects.equals(deal, other.deal)
				&& Double.doubleToLongBits(deal_ticketprice) == Double.doubleToLongBits(other.deal_ticketprice)
				&& id == other.id;
	}

	public CategoryPricing() {
		super();
	}
}
