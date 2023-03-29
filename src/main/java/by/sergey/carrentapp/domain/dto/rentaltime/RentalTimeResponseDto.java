package by.sergey.carrentapp.domain.dto.rentaltime;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder
public class RentalTimeResponseDto {
    @NotNull
    Long id;
    @NotNull
    Long orderId;
    LocalDateTime startRentalTime;
    LocalDateTime endRentalTime;
}
