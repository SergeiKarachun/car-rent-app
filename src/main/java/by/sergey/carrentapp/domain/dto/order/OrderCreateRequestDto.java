package by.sergey.carrentapp.domain.dto.order;

import java.time.LocalDateTime;

public record OrderCreateRequestDto(Long userId,
                                    Long carId,
                                    String passport,
                                    LocalDateTime startRentalDate,
                                    LocalDateTime endRentalTime) {
}
