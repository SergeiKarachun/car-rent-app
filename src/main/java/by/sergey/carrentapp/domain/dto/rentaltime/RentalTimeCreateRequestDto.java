package by.sergey.carrentapp.domain.dto.rentaltime;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class RentalTimeCreateRequestDto {
    Long orderId;
    LocalDateTime startRentalTime;
    LocalDateTime endRentalTime;
}
