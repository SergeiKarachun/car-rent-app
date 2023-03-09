package by.sergey.carrentapp.domain.dto.accident;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record AccidentResponseDto(Long id,
                                  Long orderId,
                                  LocalDateTime accidentDate,
                                  String brand,
                                  String model,
                                  String carNumber,
                                  String userFirstName,
                                  String userLastName,
                                  String description,
                                  BigDecimal damage) {
}
