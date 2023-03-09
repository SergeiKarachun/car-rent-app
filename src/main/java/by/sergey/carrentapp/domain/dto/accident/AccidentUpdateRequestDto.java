package by.sergey.carrentapp.domain.dto.accident;

import java.math.BigDecimal;

public record AccidentUpdateRequestDto(String description,
                                       BigDecimal damage) {
}
