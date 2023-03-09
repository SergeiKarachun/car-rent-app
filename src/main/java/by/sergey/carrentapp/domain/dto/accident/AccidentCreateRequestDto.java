package by.sergey.carrentapp.domain.dto.accident;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccidentCreateRequestDto(Long orderId,
                                       LocalDateTime accidentDate,
                                       String description,
                                       BigDecimal damage) {
}
