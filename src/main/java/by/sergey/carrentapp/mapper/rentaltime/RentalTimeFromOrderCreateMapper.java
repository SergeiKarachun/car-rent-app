package by.sergey.carrentapp.mapper.rentaltime;

import by.sergey.carrentapp.domain.dto.order.OrderCreateRequestDto;
import by.sergey.carrentapp.domain.entity.RentalTime;
import by.sergey.carrentapp.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class RentalTimeFromOrderCreateMapper implements CreateMapper<OrderCreateRequestDto, RentalTime> {
    @Override
    public RentalTime mapToEntity(OrderCreateRequestDto requestDto) {
        return RentalTime.builder()
                .startRentalDate(requestDto.getStartRentalDate())
                .endRentalDate(requestDto.getStartRentalDate())
                .build();
    }
}
