package by.sergey.carrentapp.mapper.rentaltime;

import by.sergey.carrentapp.domain.dto.rentaltime.RentalTimeCreateRequestDto;
import by.sergey.carrentapp.domain.entity.RentalTime;
import by.sergey.carrentapp.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class RentalTimeCreateMapper implements CreateMapper<RentalTimeCreateRequestDto, RentalTime> {

    @Override
    public RentalTime mapToEntity(RentalTimeCreateRequestDto requestDto) {
        return RentalTime.builder()
                .startRentalDate(requestDto.getStartRentalTime())
                .endRentalDate(requestDto.getEndRentalTime())
                .build();
    }
}
