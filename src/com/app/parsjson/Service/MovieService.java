package com.app.parsjson.service;

import java.util.List;

import com.app.parsjson.MovieInfo;

public interface MovieService {
	List<MovieInfo> getMovieList();
    List<MovieInfo> searchMovie(String name);
	MovieInfo getMovie(long id);

}
