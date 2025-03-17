package com.app.movie.trade.entities;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Table(name = "transaction_details")
@Entity
public class Transaction {
	@Id
	private String transaction_id;
	@Nonnull
	private String status;
	private String bank_name;
	@Nonnull
	private LocalDate transaction_date;
	@NotNull(message = "investment id cannot be null")
	private String investment_id;
	private double amount;

	@OneToOne(mappedBy = "transaction")
	@JsonIgnore
	private Investment Investment;

	public Transaction() {
		super();
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public LocalDate getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(LocalDate transaction_date) {
		this.transaction_date = transaction_date;
	}

	public String getInvestment_id() {
		return investment_id;
	}

	public void setInvestment_id(String investment_id) {
		this.investment_id = investment_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Investment getInvestment() {
		return Investment;
	}

	public void setInvestment(Investment investment) {
		Investment = investment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Investment, amount, bank_name, investment_id, status, transaction_date, transaction_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Objects.equals(Investment, other.Investment)
				&& Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(bank_name, other.bank_name) && Objects.equals(investment_id, other.investment_id)
				&& Objects.equals(status, other.status) && Objects.equals(transaction_date, other.transaction_date)
				&& Objects.equals(transaction_id, other.transaction_id);
	}

}
