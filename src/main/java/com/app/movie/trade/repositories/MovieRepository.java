package com.app.movie.trade.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.movie.trade.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
	@Query("select movie from Movie movie where movie.language in :languages")
	public List<Movie> fetchMoviesByLanguage(List<String> languages);
	@Query("select movie from Movie movie where movie.isactive=true")
	public List<Movie> findAllActiveMovies();
	@Query("select movie from Movie movie where movie.name=:name and movie.isactive=true")
	public List<Movie> findMoviesByName(String name);
}
