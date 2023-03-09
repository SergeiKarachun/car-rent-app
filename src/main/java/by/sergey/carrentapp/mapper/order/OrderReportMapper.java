package by.sergey.carrentapp.mapper.order;

import by.sergey.carrentapp.domain.dto.order.OrderUserReportDto;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderReportMapper implements ResponseMapper<Order, OrderUserReportDto> {
    @Override
    public OrderUserReportDto mapToDto(Order order) {
        return OrderUserReportDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .startRentalTime(order.getRentalTime().getStartRentalDate())
                .endRentalTime(order.getRentalTime().getEndRentalDate())
                .brand(order.getCar().getBrand().getName())
                .model(order.getCar().getModel().getName())
                .color(order.getCar().getColor())
                .transmission(order.getCar().getModel().getTransmission())
                .engineType(order.getCar().getModel().getEngineType())
                .yearOfProduction(order.getCar().getYear())
                .orderStatus(order.getOrderStatus())
                .sum(order.getSum())
                .build();
    }
}
