package services;

import dataGenerator.DataManager;
import dataGenerator.MovieStoresJsonConverter;
import exception.AppException;
import model.Movie;
import model.MovieWithDateTime;
import org.jdbi.v3.core.Jdbi;
import repository.MovieRepository;
import repository.connect.DbConnect;

import java.time.LocalDate;
import java.util.List;


public class MovieService {

    private static MovieService instance = null;
    private final Jdbi connection = DbConnect.getInstance().getConnection();
    private final String jsonFile = "movieTitle.json";
    private final DataManager dataManager = new DataManager();

    private MovieService() {
        loadMoviesToDataBase(jsonFile);

    }

    public static MovieService getInstance() {
        if (instance == null) {
            instance = new MovieService();
        }
        return instance;
    }

    private void loadMoviesToDataBase(String fileName) {
        MovieStoresJsonConverter movieStoresJsonConverter = new MovieStoresJsonConverter(fileName);
        List<Movie> movies = movieStoresJsonConverter.fromJson().get();
        for (Movie movie : movies) {
            connection.withHandle(handle ->
                    handle.execute(" INSERT INTO movie (title, genre, price, duration, release_date) values (?, ?, ?, ?, ?)",
                            movie.getTitle(), movie.getGenre(), movie.getPrice(), movie.getDuration(), movie.getRelease_date()));

          /*var rov = movies.stream()
                  .peek(connection.withHandle(handle ->
                            handle.execute(" INSERT INTO movie (title, genre, price, duration, release_date) values (?, ?, ?, ?, ?)",
                            movie.getTitle(), movie.getGenre(), movie.getPrice(), movie.getDuration(), movie.getRelease_date()))).count();*/
        }
    }

    public List<MovieWithDateTime> getInfo() {
        return connection.withHandle(handle ->
                handle
                        .createQuery("select ss.id, mm.title, ss.start_date_time, mm.price, cc.name, cc.surname, cc.email " +
                                "FROM sales_stand ss JOIN movie mm " +
                                "ON ss.movie_id = mm.id " +
                                "INNER JOIN customer cc " +
                                "ON cc.id = ss.customer_id;")
                        .mapToBean(MovieWithDateTime.class)
                        .list()
        );
    }

    MovieRepository movieRepository = new MovieRepository();


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

    boolean isMoviesBaseEmpty() {
        return getAllMovies().isEmpty();
    }


    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }


    public Movie getMovieById(Integer movieId) {
        return movieRepository.findOne(movieId).orElseThrow(() -> new AppException(" Wrong ID number "));
    }

    public void addMovie(Movie movie) {
        movieRepository.add(movie);

    }

    public List<String> printAllData() {

        return connection.withHandle(handle -> handle.createQuery("SELECT release_date FROM movie;").mapTo(String.class).list()
        );

    }

    public List<Movie> getAll() {

        return connection.withHandle(handle -> handle.createQuery("SELECT * FROM movie;").mapToBean(Movie.class).list()
        );
    }


}
