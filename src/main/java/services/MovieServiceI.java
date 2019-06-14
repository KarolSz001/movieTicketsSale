package services;
import dataGenerator.MovieStoresJsonConverter;
import exception.AppException;
import model.Movie;
import model.MovieWithDateTime;
import org.jdbi.v3.core.Jdbi;
import repository.MovieRepository;
import repository.connect.DbConnect;
import java.util.List;



public class MovieServiceI {

    private static MovieServiceI instance = null;
    private final Jdbi connection = DbConnect.getInstance().getConnection();
    private final String jsonFile = "movieTitle.json";

    private MovieServiceI() {
        loadMoviesToDataBase(jsonFile);

    }

    public static MovieServiceI getInstance() {
        if (instance == null) {
            instance = new MovieServiceI();
        }
        return instance;
    }

    MovieRepository movieRepository = new MovieRepository();


    public void addMovie(Movie movie) {
        movieRepository.add(movie);

    }


    public void removeMovieById(Integer movieId) {
        movieRepository.delete(movieId);
    }


    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }


    public Movie getMovieById(Integer movieId) {
        return movieRepository.findOne(movieId).orElseThrow(() -> new AppException(" Wrong ID number "));
    }

    private void loadMoviesToDataBase(String fileName) {
        MovieStoresJsonConverter movieStoresJsonConverter = new MovieStoresJsonConverter(fileName);
        List<Movie> movies = movieStoresJsonConverter.fromJson().get();
        for (Movie movie : movies) {
            connection.withHandle(handle ->
                    handle.execute(" INSERT INTO movie (title, genre, price, duration, release_date) values (?, ?, ?, ?, ?)", movie.getTitle(), movie.getGenre(), movie.getPrice(), movie.getDuration(), movie.getRelease_date()));

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

    public List<String> printAllData(){

       return connection.withHandle( handle -> handle
       .createQuery("SELECT release_date FROM movie;")
       .mapTo(String.class)
               .list()
       );

    }
    public List<Movie> getAll() {

        return connection.withHandle(handle -> handle
                .createQuery("SELECT * FROM movie;")
                .mapToBean(Movie.class)
                .list()
        );
    }

}
