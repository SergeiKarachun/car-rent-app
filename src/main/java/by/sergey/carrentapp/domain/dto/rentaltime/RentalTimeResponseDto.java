package by.sergey.carrentapp.domain.dto.rentaltime;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RentalTimeResponseDto(Long id,
                                    Long orderId,
                                    LocalDateTime startRentalTime,
                                    LocalDateTime endRentalTime) {
}
