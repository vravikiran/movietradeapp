package com.app.movie.trade.entities;

import java.util.Objects;

import org.hibernate.validator.constraints.Range;

import jakarta.annotation.Nonnull;

public class UserDto {
	@Range(min = 1000000000L, max = 9999999999L)
	private Long mobile_no;
	private String email;
	@Nonnull
	private String first_name;
	@Nonnull
	private String last_name;

	public Long getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(Long mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, first_name, last_name, mobile_no);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDto other = (UserDto) obj;
		return Objects.equals(email, other.email) && Objects.equals(first_name, other.first_name)
				&& Objects.equals(last_name, other.last_name) && Objects.equals(mobile_no, other.mobile_no);
	}

	@Override
	public String toString() {
		return "UserDto [mobile_no=" + mobile_no + ", email=" + email + ", first_name=" + first_name + ", last_name="
				+ last_name + "]";
	}
}
