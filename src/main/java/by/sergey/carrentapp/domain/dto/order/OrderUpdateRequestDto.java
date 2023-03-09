package by.sergey.carrentapp.domain.dto.order;

import java.time.LocalDateTime;

public record OrderUpdateRequestDto(Long carId,
                                    LocalDateTime startRentalTime,
                                    LocalDateTime endRentalTime) {
}
