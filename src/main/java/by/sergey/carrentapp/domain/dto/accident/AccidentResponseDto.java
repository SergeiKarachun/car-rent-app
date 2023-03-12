package by.sergey.carrentapp.domain.dto.accident;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class AccidentResponseDto {
    Long id;
    Long orderId;
    LocalDateTime accidentDate;
    String brand;
    String model;
    String carNumber;
    String userFirstName;
    String userLastName;
    String description;
    BigDecimal damage;
}
