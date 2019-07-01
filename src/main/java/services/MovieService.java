package services;

import enums.Genre;
import services.dataGenerator.DataManager;
import exception.AppException;
import model.Movie;
import model.MovieWithDateTime;
import repository.MovieRepository;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    private void printFormattedMovie(Movie s) {
        System.out.format("%5s %50s %20s %10d %20s %15s \n", s.getId(), s.getTitle(), s.getGenre(), s.getDuration(), s.getPrice(), s.getRelease_date());
    }

    public List<MovieWithDateTime> getInfo() {
        return movieRepository.getInfo();
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


    public Movie createMovie() {
        String title = dataManager.getLine(" GIVE A TITLE ");
        Genre genre = Genre.valueOf(dataManager.getLine(" GIVE A GENRE  - > ACTION, HORROR, FANTASY, DRAMA, COMEDY ").toUpperCase());
        Double price = Double.valueOf(dataManager.getDouble(" GIVE A PRICE -> pattern ##.## "));
        Integer duration = dataManager.getInt(" GIVE NUMBER OF MINUTES TIME, DURATION TIME ");
        return Movie.builder().title(title).genre(genre).price(price).duration(duration).price(price).build();
    }

    public void sortMoviesByDurationTime() {
        getAllMovies().stream().sorted(Comparator.comparing(Movie::getDuration, Comparator.reverseOrder())).forEach(System.out::println);
    }

    public void editMovieById() {
        Integer idMovie = dataManager.getInt(" GIVE MOVIE ID TO EDIT ");
        Movie movie = createMovie();
        movieRepository.update(idMovie, movie);
    }

    public void printStatisticByMoviePrice() {

        DecimalFormat dc = new DecimalFormat("#.##");
        DoubleSummaryStatistics stats = movieRepository.findAll()
                .stream()
                .collect(Collectors.summarizingDouble(Movie::getPrice));
        System.out.println("Statistic by movie Price");
        System.out.println("-----------------------------------------------------------------------------\n");
        System.out.println(" average praise for tickets -> " + dc.format(stats.getAverage()));
        System.out.println(" average praise for tickets -> " + dc.format(stats.getMax()));
        System.out.println(" average praise for tickets -> " + dc.format(stats.getMin()));
        System.out.println("-----------------------------------------------------------------------------\n");
    }

    public void printStatisticByDurationTime() {
        DecimalFormat dc = new DecimalFormat("#.##");
        IntSummaryStatistics stats = movieRepository.findAll()
                .stream()
                .collect(Collectors.summarizingInt(Movie::getDuration));
        System.out.println("Statistic of duration Time");
        System.out.println("-----------------------------------------------------------------------------\n");
        System.out.println(" average time for tickets -> " + dc.format(stats.getAverage()));
        System.out.println(" average time for tickets -> " + dc.format(stats.getMax()));
        System.out.println(" average time for tickets -> " + dc.format(stats.getMin()));
        System.out.println("-----------------------------------------------------------------------------\n");
    }

    public void printMapOfGenreAndNumberMovies() {
        System.out.println("MAP WITH GENRE NAD NUMBER OF MOVIES");
        System.out.println("-----------------------------------------------------------------------------\n");
        movieRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Movie::getGenre))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().size()
                ))
                .entrySet().forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------------------\n");
    }


}
