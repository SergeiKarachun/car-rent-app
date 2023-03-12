package by.sergey.carrentapp.domain.dto.order;

import by.sergey.carrentapp.domain.model.Color;
import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.OrderStatus;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class OrderUserReportDto {
    Long id;
    LocalDate date;
    LocalDateTime startRentalTime;
    LocalDateTime endRentalTime;
    String brand;
    String model;
    Color color;
    Transmission transmission;
    EngineType engineType;
    Integer yearOfProduction;
    OrderStatus orderStatus;
    BigDecimal sum;
}
