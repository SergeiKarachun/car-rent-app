package by.sergey.carrentapp.domain.dto.car;

import by.sergey.carrentapp.domain.model.Color;
import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@Builder
public class CarResponseDto {
    @NotNull
    Long id;
    String brand;
    String model;
    Transmission transmission;
    EngineType engineType;
    Color color;
    Integer year;
    String carNumber;
    String vin;
    Boolean isRepaired;
    String image;
    String category;
    BigDecimal price;
}
