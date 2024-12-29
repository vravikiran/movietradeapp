package com.app.movie.trade.entities;

import java.util.Objects;

public class EmailAuthRequest {
	private String email;
	private String otp;

	public EmailAuthRequest() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, otp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailAuthRequest other = (EmailAuthRequest) obj;
		return Objects.equals(email, other.email) && Objects.equals(otp, other.otp);
	}
}
