package dataGenerator;

import control.DataManager;
import model.Customer;
import model.Movie;
import enums.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DateGenerator {

    private final String fileNameTxt = "movie.txt";
    private final Integer minRangePrice = 50;
    private final Integer maxRangePrice = 99;
    private final Integer maxAge = 99;
    private final Integer minAge = 50;
    private final MovieStoresJsonConverter movieStoresJsonConverter = new MovieStoresJsonConverter("movieTitle.json");
    private final DataManager dataManager = new DataManager();

    public DateGenerator() {
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
       /* LocalDate start = LocalDate.of(1970, Month.JANUARY, 1);
        long days = ChronoUnit.DAYS.between(start, LocalDate.now());
        return start.plusDays(new Random().nextInt((int) days + 1));*/
       return LocalDate.now();
    }

    public List<Movie> moviesGenerator() {

        List<String> titleMovies = readTxtFile();
        List<Movie> movies = new ArrayList<>();

        for (String movieTitle : titleMovies) {
            Integer id = null;
            String title = movieTitle;
            Genre genre = Genre.getRandomGenre();
            LocalDate localDate = dataGenerator();
            DecimalFormatSymbols otherSymbol = new DecimalFormatSymbols(Locale.getDefault());
            DecimalFormat dc = new DecimalFormat("#.##", otherSymbol);
            Double price = (minRangePrice + (new Random().nextDouble() * (maxRangePrice - minRangePrice)));
            price = Double.valueOf(dc.format(price));
            Integer duration = new Random().nextInt(180 - 60) + 60;
            Movie movie = new Movie().builder().id(id).title(title).genre(genre).date(localDate).price(price).duration(duration).build();
            movies.add(movie);
        }

        return movies;
    }

    public List<Customer> customersGenerator() {
        List<Customer> customers = new ArrayList<>();
        int size = dataManager.getInt(" PRESS NUMBER OF CUSTOMERS ");

        for (int i = 0; i < size; i++) {
            String name = String.valueOf(Name.randomNameGenerator());
            String surname = String.valueOf(Surname.randomSurNameGenrator());
            Integer age = new Random().nextInt(maxAge - minAge) + minAge;
            String email = emailGenerator(name, surname);

            customers.add(new Customer(null, name, surname, age, email, null));
        }
        return customers;
    }


    private enum Name {
        ADAM, DAVID, JON, SONIA, ANIA, OLA, ELA, ZOFIA, KAROl, LUDVIC, HANNA;

        public static Name randomNameGenerator() {
            int size = Name.values().length;
            return Name.values()[new Random().nextInt(size)];
        }
    }

    private enum Surname {
        KOWALSKI, NOWAK, LECH, CZECH, PRUS, KACZMAREK, LEWANODWSKI, BORUC;

        public static Surname randomSurNameGenrator() {
            int size = Surname.values().length;
            return Surname.values()[new Random().nextInt(size)];
        }
    }

    private String emailGenerator(String name, String surname) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(".");
        sb.append(surname);
        sb.append("@o2.pl");
        return sb.toString();
    }


}