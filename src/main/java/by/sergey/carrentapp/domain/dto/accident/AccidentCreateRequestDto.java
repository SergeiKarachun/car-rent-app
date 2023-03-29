package by.sergey.carrentapp.domain.dto.accident;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class AccidentCreateRequestDto {
    @NotNull
    Long orderId;
    @NotNull
    LocalDateTime accidentDate;
    @NotBlank(message = "Description is required")
    String description;
    @NotNull
    BigDecimal damage;
}
