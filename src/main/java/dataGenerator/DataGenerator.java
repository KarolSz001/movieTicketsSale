package dataGenerator;

import model.Movie;
import model.enums.Genre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    final String fileNameTxt = "movie.txt";
    final MovieStoresJsonConverter movieStoresJsonConverter = new MovieStoresJsonConverter("movieTitle.json");

    public DataGenerator() {
        List<Movie> movies = moviesGenerator();
        movieStoresJsonConverter.toJson(movies);

    }


    public List<String> readTxtFile() {
        List<String> list = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileNameTxt));
            String readlLine = bufferedReader.readLine();

            while (readlLine != null) {
                list.add(readlLine);
                readlLine = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private LocalDate dataGenerator() {
        LocalDate start = LocalDate.of(1970, Month.JANUARY, 1);
        long days = ChronoUnit.DAYS.between(start, LocalDate.now());
        return start.plusDays(new Random().nextInt((int) days + 1));
    }

    private List<Movie> moviesGenerator() {

        List<String> titleMovies = readTxtFile();
        List<Movie> movies = new ArrayList<>();

        for (String movieTitle : titleMovies) {
            Integer id = null;
            String title = movieTitle;
            Genre genre = Genre.getRandomGenre();
            LocalDate localDate = dataGenerator();
            Double price = 100 + (new Random().nextDouble() * (200 - 100));
            Integer duration = new Random().nextInt(180 - 60) + 60;
            Movie movie = new Movie().builder().id(id).title(title).genre(genre).date(localDate).price(price).duration(duration).build();
            movies.add(movie);
        }
        return movies;
    }
}
