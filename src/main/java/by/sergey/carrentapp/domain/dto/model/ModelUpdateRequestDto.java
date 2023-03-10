package by.sergey.carrentapp.domain.dto.model;

import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.Value;

@Value
public class ModelUpdateRequestDto {
    String name;
    Transmission transmission;
    EngineType engineType;
}
