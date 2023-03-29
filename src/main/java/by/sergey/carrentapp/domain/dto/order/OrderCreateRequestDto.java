package by.sergey.carrentapp.domain.dto.order;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class OrderCreateRequestDto {
    @NotNull
    Long userId;
    @NotNull
    Long carId;
    @NotBlank(message = "Passport is required")
    String passport;
    @NotNull
    LocalDateTime startRentalDate;
    @NotNull
    LocalDateTime endRentalDate;
}
