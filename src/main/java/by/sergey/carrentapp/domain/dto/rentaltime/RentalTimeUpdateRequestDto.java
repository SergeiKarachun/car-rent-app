package by.sergey.carrentapp.domain.dto.rentaltime;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class RentalTimeUpdateRequestDto {
    @NotNull
    LocalDateTime startRentalTime;
    @NotNull
    LocalDateTime endRentalTime;
}
