package by.sergey.carrentapp.domain.dto.order;

import by.sergey.carrentapp.domain.dto.car.CarResponseDto;
import by.sergey.carrentapp.domain.dto.user.UserResponseDto;
import by.sergey.carrentapp.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class OrderResponseDto {
    Long id;
    UserResponseDto user;
    CarResponseDto car;
    LocalDate date;
    OrderStatus orderStatus;
    BigDecimal sum;
    LocalDateTime startRentalTime;
    LocalDateTime endRentalTime;
}
