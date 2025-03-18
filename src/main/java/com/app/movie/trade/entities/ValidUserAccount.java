package com.app.movie.trade.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "valid_user_accounts")
@Entity
public class ValidUserAccount {
	@Id
	private long mobileno;

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mobileno);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidUserAccount other = (ValidUserAccount) obj;
		return mobileno == other.mobileno;
	}
}
