package com.app.movie.trade.entities;

import java.io.Serializable;
import java.util.Objects;

public class InvestmentRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private int dealid;
	private double amounttopay;
	private long mobileno;
	private int trans_digits;

	public int getTrans_digits() {
		return trans_digits;
	}

	public void setTrans_digits(int trans_digits) {
		this.trans_digits = trans_digits;
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public int getDealid() {
		return dealid;
	}

	public void setDealid(int dealid) {
		this.dealid = dealid;
	}

	public double getAmounttopay() {
		return amounttopay;
	}

	public void setAmounttopay(double amounttopay) {
		this.amounttopay = amounttopay;
	}

	public InvestmentRequest() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(amounttopay, dealid, mobileno, trans_digits);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvestmentRequest other = (InvestmentRequest) obj;
		return Double.doubleToLongBits(amounttopay) == Double.doubleToLongBits(other.amounttopay)
				&& dealid == other.dealid && mobileno == other.mobileno && trans_digits == other.trans_digits;
	}
}
