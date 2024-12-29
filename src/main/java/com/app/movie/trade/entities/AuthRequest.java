package com.app.movie.trade.entities;

import java.util.Objects;

public class AuthRequest {
	private String mobileNo;
	private String otp;

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mobileNo, otp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthRequest other = (AuthRequest) obj;
		return Objects.equals(mobileNo, other.mobileNo) && Objects.equals(otp, other.otp);
	}

	public AuthRequest() {
		super();
	}

	@Override
	public String toString() {
		return "AuthRequest [mobileNo=" + mobileNo + ", otp=" + otp + "]";
	}

}
