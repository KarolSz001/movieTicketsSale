package test1;

import enums.Genre;
import exception.AppException;
import model.Customer;
import model.Movie;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import repository.MovieRepository;
import services.MovieService;

import java.time.LocalDate;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestMovieService {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    @DisplayName(" find all movies ")
    public void test1() {
        //GIVEN
        Mockito
                .when(movieRepository.findAll())
                .thenReturn(List.of(Movie.builder().title("RAMBO").genre(Genre.ACTION).price(100.0).release_date(LocalDate.now()).build()));

        // WHEN
        List<Movie> customers = movieService.getAllMovies();

        //THEN
        Assertions.assertEquals(1, customers.size());
    }

    @Test
    @DisplayName(" add movie null ")
    public void test2() {

        AppException e = Assertions.assertThrows(AppException.class, () -> movieService.addMovie(null));
        Assertions.assertEquals("add movie null arg", e.getMessage());

    }

    @Test
    @DisplayName("find movie by id number")
    public void test3() {
        // GIVEN
        Mockito
                .when(movieRepository.findOne(1))
                .thenReturn(java.util.Optional.ofNullable(Movie.builder().title("RAMBO").genre(Genre.ACTION).price(100.0).release_date(LocalDate.now()).build()));
        // WHEN
        Movie movie = (movieService.getMovieById(1));
        // THEN
        Assertions.assertTrue(movie.getPrice() == 100);

    }

    @Test
    @DisplayName(" delete movie with null id ")
    public void test4() {

        AppException e = Assertions.assertThrows(AppException.class, () -> movieService.removeMovieById(null));
        Assertions.assertEquals("null id number", e.getMessage());
    }

    @Test
    @DisplayName(" should return movie while get id")
    public void test5() {

        Movie expectedMovie = Movie.builder().title("RAMBO").genre(Genre.ACTION).price(100.0).release_date(LocalDate.now()).build();
        expectedMovie.setId(1);
        // GIVEN
        Mockito
                .when(movieRepository.findOne(1))
                .thenReturn(java.util.Optional.ofNullable(Movie.builder().title("RAMBO").genre(Genre.ACTION).price(100.0).release_date(LocalDate.now()).id(1).build()));

        // when
        Movie movieResult = movieService.getMovieById(1);
        // then
        Assert.assertEquals(expectedMovie, movieResult);
    }

    @Test
    @DisplayName(" should return empty Optional while get id")
    public void test7() {

        AppException e = Assertions.assertThrows(AppException.class, () -> movieService.getMovieById(1));
        Assertions.assertEquals(" Wrong ID number ", e.getMessage());

    }


}
