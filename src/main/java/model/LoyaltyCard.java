package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoyaltyCard {
    Integer id;
    LocalDate expirationDate;
    Double discount;
    Integer moviesNumber;
}
