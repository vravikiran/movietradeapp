package com.app.movie.trade.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.movie.trade.entities.Movie;
import com.app.movie.trade.exceptions.MovieNotFoundException;
import com.app.movie.trade.services.MovieService;

@RestController
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	MovieService movieService;

	@GetMapping("/languages")
	public ResponseEntity<List<Movie>> getMoviesByLangauge(@RequestParam List<String> languages) {
		List<Movie> movies = movieService.getMoviesByLanguage(languages);
		return ResponseEntity.ok(movies);
	}

	@GetMapping("/movieid")
	public ResponseEntity<Movie> getMovieById(@RequestParam int movieid) throws MovieNotFoundException {
		Movie movie = movieService.getMovieById(movieid);
		return ResponseEntity.ok(movie);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
	public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
		Movie createdMovie = movieService.createMovie(movie);
		return ResponseEntity.ok(createdMovie);
	}

	@PatchMapping("/update")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
	public ResponseEntity<Movie> updateMovie(@RequestParam int movieid,@RequestBody Map<String, String> valuesToUpdate)
			throws MovieNotFoundException {
		Movie updatedMovie = movieService.updateMovie(movieid, valuesToUpdate);
		return ResponseEntity.ok(updatedMovie);
	}

	@DeleteMapping("/deactivate")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
	public ResponseEntity<String> deactivateMovie(@RequestParam int movieid) throws MovieNotFoundException {
		movieService.deactivateMovie(movieid);
		return ResponseEntity.ok("Movie deactivated successfully");
	}

	@GetMapping
	public ResponseEntity<List<Movie>> getMoviesList() {
		List<Movie> movies = movieService.getAllActiveMovies();
		return ResponseEntity.ok(movies);
	}
	
	@GetMapping("/name")
	public ResponseEntity<List<Movie>> getMoviesByName(@RequestParam String name) {
		List<Movie> movies = movieService.getMoviesByName(name);
		return ResponseEntity.ok(movies);
	}
	
}
