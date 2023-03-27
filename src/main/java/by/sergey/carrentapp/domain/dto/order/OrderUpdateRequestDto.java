package by.sergey.carrentapp.domain.dto.order;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class OrderUpdateRequestDto {
    Long carId;
    LocalDateTime startRentalDate;
    LocalDateTime endRentalDate;
}
