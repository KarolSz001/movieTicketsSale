package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import enums.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    Integer id;
    String title;
    Genre genre;
    Double price;
    Integer duration;
    LocalDate date;

}
