package by.sergey.carrentapp.domain.dto.accident;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
public class AccidentUpdateRequestDto {
    @NotBlank(message = "Description is required")
    String description;
    @NotNull
    BigDecimal damage;
}
