package by.sergey.carrentapp.domain.dto.rentaltime;

import java.time.LocalDateTime;

public record RentalTimeCreateRequestDto(Long orderId,
                                         LocalDateTime startRentalTime,
                                         LocalDateTime endRentalTime) {
}
