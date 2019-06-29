package services;

import enums.Genre;
import model.Customer;
import services.dataGenerator.DataManager;
import exception.AppException;
import model.Movie;
import model.MovieWithDateTime;
import repository.MovieRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class MovieService {

    private static MovieService instance;
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
            System.out.println("-----------------------------------------------------------------------------\n");
            System.out.printf("%5s %40s %25s %15s %15s %15s", "MOVIE ID", "TITLE", "GRADE", "DURATION", "PRICE", "RELEASE DATA\n");
            getAllMovies().stream().filter(f -> f.getRelease_date().equals(LocalDate.now())).forEach(this::printFormattedMovie);
            System.out.println("-----------------------------------------------------------------------------\n");
        }
    }
    private void printFormattedMovie(Movie s){
        System.out.format("%5s %50s %20s %10d %20s %15s \n",s.getId(),s.getTitle(),s.getGenre(),s.getDuration(),s.getPrice(), s.getRelease_date());
    }

    public List<MovieWithDateTime> getInfo(){ return movieRepository.getInfo(); }

    boolean isMoviesBaseEmpty() { return getAllMovies().isEmpty(); }

    public List<Movie> getAllMovies() { return movieRepository.findAll(); }

    public Movie getMovieById(Integer movieId) { return movieRepository.findOne(movieId).orElseThrow(() -> new AppException(" Wrong ID number ")); }

    public void addMovie(Movie movie) { movieRepository.add(movie); }


    public Movie createMovie(){
        String title  = dataManager.getLine(" GIVE A TITLE ");
        Genre genre = Genre.valueOf(dataManager.getLine(" GIVE A GENRE  - > ACTION, HORROR, FANTASY, DRAMA, COMEDY ").toUpperCase());
        Double price = Double.valueOf(dataManager.getDouble(" GIVE A PRICE -> patern ##.## "));
        Integer duration = dataManager.getInt(" GIVE NUMBER OF MINUTES TIME, DURATION TIME ");
        return Movie.builder().title(title).genre(genre).price(price).duration(duration).price(price).build();
    }

    public void sortMoviesByDurationTime(){
        getAllMovies().stream().sorted(Comparator.comparing(Movie::getDuration,Comparator.reverseOrder())).forEach(System.out::println);
    }

    public void editMovieById(){
        Integer idMovie = dataManager.getInt(" GIVE MOVIE ID TO EDIT ");
        Movie movie  = createMovie();
        movieRepository.update(idMovie, movie);
    }




}
