package by.sergey.carrentapp.domain.dto.model;

import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class ModelResponseDto {
    @NotNull
    Long id;
    String brand;
    String name;
    Transmission transmission;
    EngineType engineType;
}
