package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieWithDateTime {

    private Integer id;
    private String title;
    private LocalDateTime start_date_time;
    private Double price;
    private String name;
    private String surname;
    private String email;

}
