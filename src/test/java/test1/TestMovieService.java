package test1;

import enums.Genre;
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




}
