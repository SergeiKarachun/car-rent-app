package by.sergey.carrentapp.domain.dto.accident;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class AccidentUpdateRequestDto {
    String description;
    BigDecimal damage;
}
