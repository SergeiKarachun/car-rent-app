package by.sergey.carrentapp.domain.dto.rentaltime;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class RentalTimeResponseDto {
    Long id;
    Long orderId;
    LocalDateTime startRentalTime;
    LocalDateTime endRentalTime;
}
