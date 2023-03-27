package by.sergey.carrentapp.mapper.order;

import by.sergey.carrentapp.domain.dto.order.OrderResponseDto;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.mapper.ResponseMapper;
import by.sergey.carrentapp.mapper.car.CarResponseMapper;
import by.sergey.carrentapp.mapper.user.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderResponseMapper implements ResponseMapper<Order, OrderResponseDto> {

    private final UserResponseMapper userResponseMapper;
    private final CarResponseMapper carResponseMapper;

    @Override
    public OrderResponseDto mapToDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .user(userResponseMapper.mapToDto(order.getUser()))
                .car(carResponseMapper.mapToDto(order.getCar()))
                .date(order.getDate())
                .orderStatus(order.getOrderStatus())
                .sum(order.getSum())
                .startRentalDate(order.getRentalTime().getStartRentalDate())
                .endRentalDate(order.getRentalTime().getEndRentalDate())
                .build();
    }
}
