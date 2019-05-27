package repository.connect;

import dataGenerator.MovieStoresJsonConverter;
import model.Customer;
import model.Movie;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class DbConnect {

    private static DbConnect ourInstance = new DbConnect();

    public static DbConnect getInstance() {
        return ourInstance;
    }

    private final String USERNAME = "root";
    private final String PASSWORD = "admin";
    private final String DATABASE = "cinema_db";
    private final String URI = "jdbc:mysql://localhost:3306/" + DATABASE
            + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

    private final Jdbi connection;

    private DbConnect() {
        connection = Jdbi.create(URI, USERNAME, PASSWORD);
        cleanTables();
        createTables();
        loadMoviesTODataBase();
        loadCustomerTODataBase(new Customer(null, "roman", "nowak", 22, "roma.nowak@o2.pl", null));
    }

    public Jdbi getConnection() {
        return connection;
    }

    private void loadMoviesTODataBase() {
        MovieStoresJsonConverter movieStoresJsonConverter = new MovieStoresJsonConverter("movieTitle.json");
        List<Movie> movies = movieStoresJsonConverter.fromJson().get();
        for (Movie movie : movies) {
            connection.withHandle(handle ->
                    handle.execute(" INSERT INTO movie (title, genre, price, duration, release_date) values (?, ?, ?, ?, ?)", movie.getTitle(), movie.getGenre(), movie.getPrice(), movie.getDuration(), movie.getDate()));

        }
    }

    public void cleanTables() {

        connection.useHandle(handle -> {

            handle.execute("DROP TABLE IF EXISTS sales_stand");
            handle.execute("DROP TABLE IF EXISTS customer");
            handle.execute("DROP TABLE IF EXISTS loyalty_card");
            handle.execute("DROP TABLE IF EXISTS movie");

        });
    }

    public void loadCustomerTODataBase(Customer customer) {
        connection.withHandle(handle ->
                handle.execute("INSERT INTO customer (name, surname, age, email, loyalty_card_id) values (?, ?, ?, ?, ?)", customer.getName(), customer.getSurname(), customer.getAge(), customer.getEmail(), customer.getLoyalty_card_id()));
    }

    private void createTables() {

        connection.useHandle(handle -> {

            handle.execute(
                    "create table IF NOT EXISTS movie (" +
                            "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                            "title VARCHAR(50) NOT NULL, " +
                            "genre VARCHAR(50) NOT NULL, " +
                            "price DECIMAL(4,2) NOT NULL, " +
                            "duration INT(11) NOT NULL, " +
                            "release_date DATE NOT NULL " +
                            ")"
            );
            handle.execute(
                    "CREATE TABLE IF NOT EXISTS loyalty_card (" +
                            "id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                            "expiration_date DATE NOT NULL, " +
                            "discount DECIMAL(2,0) NOT NULL, " +
                            "movies_number INT(11) NOT NULL, " +
                            "current_movies_number INT(11) NOT NULL DEFAULT 0 " +
                            ");"
            );

            handle.execute(
                    "CREATE TABLE IF NOT EXISTS customer (" +
                            "id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(50) NOT NULL, " +
                            "surname VARCHAR(50) NOT NULL, " +
                            "age INT(11) NOT NULL, " +
                            "email VARCHAR(50), " +
                            "loyalty_card_id INT(11), " +
                            "FOREIGN KEY (loyalty_card_id) REFERENCES loyalty_card(id) ON DELETE CASCADE ON UPDATE CASCADE " +
                            ");"
            );


            handle.execute(
                    "CREATE TABLE IF NOT EXISTS sales_stand (" +
                            "id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                            "customer_id INT(11) NOT NULL, " +
                            "movie_id INT(11) NOT NULL, " +
                            "start_date_time TIMESTAMP NOT NULL, " +
                            "discount BOOL NOT NULL DEFAULT FALSE, " +
                            "FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                            "FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE ON UPDATE CASCADE " +
                            ");"
            );


        });
    }


}

