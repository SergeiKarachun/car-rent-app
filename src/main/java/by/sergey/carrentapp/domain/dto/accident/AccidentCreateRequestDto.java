package by.sergey.carrentapp.domain.dto.accident;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class AccidentCreateRequestDto {
    Long orderId;
    LocalDateTime accidentDate;
    String description;
    BigDecimal damage;
}
