package by.sergey.carrentapp.domain.dto.rentaltime;

import java.time.LocalDateTime;

public record RentalTimeUpdateRequestDto(LocalDateTime startRentalTime,
                                         LocalDateTime endRentalTime) {
}
