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


public class SalesStand {
    Integer id;
    Integer customerId;
    Integer movieId;
    LocalDateTime startDateTime;
}
