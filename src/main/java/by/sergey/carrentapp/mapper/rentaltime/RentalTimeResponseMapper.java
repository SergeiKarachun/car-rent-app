package by.sergey.carrentapp.mapper.rentaltime;

import by.sergey.carrentapp.domain.dto.rentaltime.RentalTimeResponseDto;
import by.sergey.carrentapp.domain.entity.RentalTime;
import by.sergey.carrentapp.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class RentalTimeResponseMapper implements ResponseMapper<RentalTime, RentalTimeResponseDto> {
    @Override
    public RentalTimeResponseDto mapToDto(RentalTime rentalTime) {
        return RentalTimeResponseDto.builder()
                .id(rentalTime.getId())
                .orderId(rentalTime.getOrder().getId())
                .startRentalTime(rentalTime.getStartRentalDate())
                .endRentalTime(rentalTime.getEndRentalDate())
                .build();
    }
}
