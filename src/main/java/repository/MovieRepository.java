package repository;

import exception.AppException;
import model.Movie;
import repository.generic.AbstractCrudRepository;

import java.util.List;


public class MovieRepository extends AbstractCrudRepository<Movie, Integer> {


    @Override
    public void add(Movie movie) {

        if(movie == null){
            throw new AppException(" add wrong argument - > null");
        }

        connection.withHandle(handle ->
        handle.execute("INSERT INTO movie (title, genre, price, duration, release_date) values (?, ?, ?, ?, ?)", movie.getTitle(), movie.getGenre(), movie.getPrice(), movie.getDuration(), movie.getRelease_date()));

    }

    @Override
    public void update(Integer integer, Movie movie) {

        connection.withHandle(handle ->
                handle
                        .createUpdate("UPDATE movie set title = :title, genre = :genre , price = :price, duration = :duration, release_date =: release_date WHERE id = :id")
                        .bind("title",movie.getTitle())
                        .bind("genre",movie.getGenre())
                        .bind("price",movie.getPrice())
                        .bind("duration", movie.getDuration())
                        .bind("dddd", movie.getRelease_date())
                        .execute()
        );

    }



}
