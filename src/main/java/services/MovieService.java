package services;

import services.dataGenerator.DataManager;
import services.dataGenerator.MovieStoresJsonConverter;
import exception.AppException;
import model.Movie;
import model.MovieWithDateTime;
import org.jdbi.v3.core.Jdbi;
import repository.MovieRepository;
import repository.connect.DbConnect;
import java.time.LocalDate;
import java.util.List;

public class MovieService {

    private static MovieService instance;
    private final Jdbi connection = DbConnect.getInstance().getConnection();
    private final String jsonFile = "movieTitle.json";
    private final DataManager dataManager = new DataManager();
    private final MovieRepository movieRepository = new MovieRepository();

    private MovieService() {
        movieRepository.loadMoviesToDataBase(jsonFile);

    }

    public static MovieService getInstance() {
        if (instance == null) {
            instance = new MovieService();
        }
        return instance;
    }


    public void removeMovieById(Integer movieId) {
        movieRepository.delete(movieId);
    }

    public void showMovieById(Integer id) {
        System.out.println(getMovieById(id));
    }

    public void removeMovieById() {
        Integer id = dataManager.getInt(" PRESS ID MOVIE NUMBER");
        removeMovieById(id);
    }

    public void showAllMoviesToday() {
        if (isMoviesBaseEmpty()) {
            System.out.println(" DATABASE IS EMPTY \n");
        } else {
            getAllMovies().stream().filter(f -> f.getRelease_date().equals(LocalDate.now())).forEach(System.out::println);
        }
    }

    public List<MovieWithDateTime> getInfo(){ return movieRepository.getInfo(); }

    boolean isMoviesBaseEmpty() { return getAllMovies().isEmpty(); }

    public List<Movie> getAllMovies() { return movieRepository.findAll(); }

    public Movie getMovieById(Integer movieId) { return movieRepository.findOne(movieId).orElseThrow(() -> new AppException(" Wrong ID number ")); }

    public void addMovie(Movie movie) { movieRepository.add(movie); }




}
