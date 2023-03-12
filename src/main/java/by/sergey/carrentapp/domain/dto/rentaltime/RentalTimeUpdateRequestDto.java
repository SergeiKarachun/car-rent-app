package by.sergey.carrentapp.domain.dto.rentaltime;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class RentalTimeUpdateRequestDto {
    LocalDateTime startRentalTime;
    LocalDateTime endRentalTime;
}
