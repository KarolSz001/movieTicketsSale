package services.dataGenerator;

import model.Customer;
import model.Movie;
import enums.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

public class DataGenerator {

    private static DataGenerator instance;
    private final String fileNameTxt = "titlesMovie.txt";
    private final Integer minRangePrice = 50;
    private final Integer maxRangePrice = 99;
    private final Integer maxAge = 99;
    private final Integer minAge = 50;
    private final MovieStoresJsonConverter movieStoresJsonConverter = new MovieStoresJsonConverter("movieTitle.json");
    private final DataManager dataManager = new DataManager();

    private DataGenerator() {
        List<Movie> movies = moviesGenerator();
        movieStoresJsonConverter.toJson(movies);
    }

    public static DataGenerator getInstance() {
        if(instance == null) {
            instance = new DataGenerator();
        }
        return instance;
    }



    public List<String> readTxtFile() {
        List<String> list = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileNameTxt));
            String readLine = bufferedReader.readLine();

            while (readLine != null) {
                list.add(readLine);
                readLine = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private LocalDate dateGenerator() {

        LocalDate today = LocalDate.now();
        LocalDate past = today.minusMonths(1);
        LocalDate future = today.plusMonths(1);
        LocalDate[] localDates = {past, today, future};
        int size = localDates.length;
        return localDates[new Random().nextInt(size)];
    }

    public List<Movie> moviesGenerator() {
        List<String> titleMovies = readTxtFile();
        List<Movie> movies = new ArrayList<>();
        titleMovies.stream().peek(p -> movies.add(singleMovieGenerator(p))).collect(Collectors.toList());
        return movies;
    }

    private Movie singleMovieGenerator(String title) {
        Genre genre = Genre.getRandomGenre();
        LocalDate localDate = dateGenerator();
        DecimalFormatSymbols otherSymbol = new DecimalFormatSymbols(Locale.getDefault());
        DecimalFormat dc = new DecimalFormat("#.##", otherSymbol);
        Double price = (minRangePrice + (new Random().nextDouble() * (maxRangePrice - minRangePrice)));
        price = Double.valueOf(dc.format(price));
        Integer duration = new Random().nextInt(180 - 60) + 60;
        return new Movie().builder().id(null).title(title).genre(genre).price(price).duration(duration).release_date(localDate).build();
    }

    public List<Customer> customersGenerator() {
        List<Customer> customers = new ArrayList<>();
        int size = dataManager.getInt(" PRESS NUMBER OF CUSTOMERS ");
        for (int i = 0; i < size; i++) {
            Customer customer = singleCustomerGenerator();
            customers.add(customer);
        }
        return customers;
    }

    public Customer singleCustomerGenerator() {
        String name = String.valueOf(Name.randomNameGenerator());
        String surname = String.valueOf(Surname.randomSurNameGenrator());
        Integer age = new Random().nextInt(maxAge - minAge) + minAge;
        String email = emailGenerator(name, surname);

        return Customer.builder().name(name).surname(surname).age(age).email(email).build();
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
        sb.append(name.toLowerCase());
        sb.append(".");
        sb.append(surname.toLowerCase());
        sb.append("@gmail.com");
        return sb.toString();
    }


}
