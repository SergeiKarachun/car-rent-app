package by.sergey.carrentapp.domain.dto.order;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class OrderCreateRequestDto {
    Long userId;
    Long carId;
    String passport;
    LocalDateTime startRentalDate;
    LocalDateTime endRentalDate;
}
