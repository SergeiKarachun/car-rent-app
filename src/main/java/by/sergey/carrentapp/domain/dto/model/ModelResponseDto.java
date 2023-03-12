package by.sergey.carrentapp.domain.dto.model;

import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ModelResponseDto {
    Long id;
    String brand;
    String name;
    Transmission transmission;
    EngineType engineType;
}
