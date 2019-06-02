package service;

import api.MovieService;
import dataGenerator.MovieStoresJsonConverter;
import exception.AppException;
import model.Movie;
import org.jdbi.v3.core.Jdbi;
import repository.MovieRepository;
import repository.connect.DbConnect;

import java.util.List;


public class MovieServiceImpl implements MovieService {

    private static MovieServiceImpl instance = null;
    private final Jdbi connection = DbConnect.getInstance().getConnection();
    private final String jsonFile = "movieTitle.json";

    private MovieServiceImpl() {
        loadMoviesToDataBase(jsonFile);

    }

    public static MovieServiceImpl getInstance() {
        if (instance == null) {
            instance = new MovieServiceImpl();
        }
        return instance;
    }

    MovieRepository movieRepository = new MovieRepository();

    @Override
    public void addMovie(Movie movie) {
        movieRepository.add(movie);

    }

    @Override
    public void removeMovieById(Integer movieId) {
        movieRepository.delete(movieId);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.getAll();
    }

    @Override
    public Movie getMovieById(Integer movieId) {
        return movieRepository.findOne(movieId).orElseThrow(() -> new AppException("Wrong ID number"));
    }

    private void loadMoviesToDataBase(String fileName) {
        MovieStoresJsonConverter movieStoresJsonConverter = new MovieStoresJsonConverter(fileName);
        List<Movie> movies = movieStoresJsonConverter.fromJson().get();
        for (Movie movie : movies) {
            connection.withHandle(handle ->
                    handle.execute(" INSERT INTO movie (title, genre, price, duration, release_date) values (?, ?, ?, ?, ?)", movie.getTitle(), movie.getGenre(), movie.getPrice(), movie.getDuration(), movie.getDate()));

        }
    }
}
