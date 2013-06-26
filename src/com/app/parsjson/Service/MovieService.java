package com.app.parsjson.Service;

import java.util.List;

import com.app.parsjson.MovieInfo;

public interface MovieService {
	public List<MovieInfo> getMovieList(String query);
	public MovieInfo getMovie(long id);
}
