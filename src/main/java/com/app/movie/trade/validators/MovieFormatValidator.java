package com.app.movie.trade.validators;

import com.app.movie.trade.enums.MovieFormatEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MovieFormatValidator implements ConstraintValidator<IsValidMovieFormat, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && MovieFormatEnum.movieFormatValues().keySet().contains(value.toUpperCase());
	}
}
