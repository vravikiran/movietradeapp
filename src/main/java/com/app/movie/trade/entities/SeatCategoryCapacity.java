package com.app.movie.trade.entities;

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
@Table(name = "seat_category_capacity")
public class SeatCategoryCapacity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int category_id;

	@ManyToOne
	@JoinColumn(name = "theatre_id")
	@JsonIgnore
	private Theatre theatre;

	private String category_name;
	private int capacity;

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(capacity, category_id, category_name, theatre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatCategoryCapacity other = (SeatCategoryCapacity) obj;
		return Objects.equals(capacity, other.capacity) && category_id == other.category_id
				&& Objects.equals(category_name, other.category_name) && Objects.equals(theatre, other.theatre);
	}

	public SeatCategoryCapacity() {
		super();
	}
}
