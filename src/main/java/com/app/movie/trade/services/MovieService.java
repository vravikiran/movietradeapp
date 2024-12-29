package com.app.movie.trade.services;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.Movie;
import com.app.movie.trade.exceptions.MovieNotFoundException;
import com.app.movie.trade.repositories.MovieRepository;

@Service
public class MovieService {
	@Autowired
	MovieRepository movieRepository;
	Logger logger = LoggerFactory.getLogger(MovieService.class);

	public Movie createMovie(Movie movie) throws DataIntegrityViolationException {
		logger.info("Creation of movie started with details :: " + movie.toString());
		Movie createdMovie = null;
		try {
			createdMovie = movieRepository.save(movie);
			logger.info("Movie created successfully");
		} catch (DataIntegrityViolationException exception) {
			logger.error(
					"Creation of movie failed as combination of movie name/language/format already exists. Exceptin details :: "
							+ exception.getMessage());
			throw new DataIntegrityViolationException("combination of movie name/language/format already exists");
		}
		return createdMovie;
	}

	public void deactivateMovie(int movieid) throws MovieNotFoundException {
		logger.info("Started deactivating movie with given id :: " + movieid);
		if (movieRepository.existsById(movieid)) {
			Movie movie = movieRepository.getReferenceById(movieid);
			movie.setIsactive(false);
			logger.info("succesfully deactivated movie :: " + movie.toString());
			movieRepository.save(movie);
		} else {
			logger.info("Movie with given id " + movieid + " doesn't exists");
			throw new MovieNotFoundException("Movie with given id doesn't exists");
		}
	}

	public Movie updateMovie(int movieid, Map<String, String> valuesToUpdate)
			throws MovieNotFoundException, NoSuchElementException {
		logger.info("Updating movie details with id :: " + movieid);
		if (movieRepository.existsById(movieid)) {
			Movie movie = movieRepository.getReferenceById(movieid);
			try {
				movie.updateValues(movie, valuesToUpdate);
			} catch (NoSuchElementException exception) {
				logger.info("one or more fields to be updated are not valid for a given movie with id :: " + movieid);
				throw new NoSuchElementException("One or more fields are not valid");
			}
			movieRepository.save(movie);
			logger.info("Movie updated succesfully with id :: " + movieid);
			return movie;
		} else {
			throw new MovieNotFoundException("Movie with given id doesn't exists");
		}
	}

	public List<Movie> getMoviesByLanguage(List<String> languages) {
		logger.info("Fetching movies based on langueages :: " + languages.toString());
		return movieRepository.fetchMoviesByLanguage(languages);
	}

	public Movie getMovieById(int movieid) throws MovieNotFoundException {
		logger.info("Fetching movie details with id :: " + movieid);
		if (movieRepository.existsById(movieid)) {
			logger.info("Fetched movie details successfully with id :: " + movieid);
			return movieRepository.getReferenceById(movieid);
		} else {
			logger.info("Movie with given Id :: " + movieid + "doesn't exists");
			throw new MovieNotFoundException("Movie with given Id doesn't exists");
		}
	}

	public List<Movie> getAllActiveMovies() {
		logger.info("Fetching all the movies which have active deals");
		return movieRepository.findAllActiveMovies();
	}

	public List<Movie> getMoviesByName(String name) {
		logger.info("Fetching movies based on given name :: " + name);
		return movieRepository.findMoviesByName(name);
	}
}
