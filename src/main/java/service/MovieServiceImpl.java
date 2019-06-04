package service;

import api.MovieService;
import dataGenerator.MovieStoresJsonConverter;
import exception.AppException;
import model.Movie;
import model.MovieWithDateTime;
import org.jdbi.v3.core.Jdbi;
import repository.MovieRepository;
import repository.connect.DbConnect;

import java.time.LocalDateTime;
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
/*SELECT
		title,
		start_date_time
FROM sales_stand
JOIN movie
ON  sales_stand.id = sales_stand.movie_id;  */

//select p.id, p.name, p.goals, t.id, t.name, t.points from player p join team t on p.team_id = t.id

    public List<MovieWithDateTime> getInfo() {
        return connection.withHandle(handle ->
                handle
                        .createQuery("select ss.id, mm.title, ss.start_date_time, mm.price FROM sales_stand ss JOIN movie mm ON ss.movie_id = mm.id;")
                       /* .map((rs, cts) -> MovieWithDateTime.builder()
                                .id(rs.getInt("ss.id"))
                                .title(rs.getString("mm.title"))
                                .watchTime(LocalDateTime.("ss.start_date_time"))
                                .build())
                        .list()
*/
                        .mapToBean(MovieWithDateTime.class)
                        .list()
        );
    }
}
