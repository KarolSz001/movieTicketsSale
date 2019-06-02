package api;


import model.Movie;

import java.util.List;

public interface MovieService {

    void addMovie(Movie movie);
    void removeMovieById(Integer movieId);

    List<Movie> getAllMovies();
    Movie getMovieById(Integer customerId);

}
