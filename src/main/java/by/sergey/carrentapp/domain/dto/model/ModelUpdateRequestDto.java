package by.sergey.carrentapp.domain.dto.model;

import by.sergey.carrentapp.annotation.EnumNamePattern;
import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class ModelUpdateRequestDto {
    @NotBlank(message = "Name is required")
    String name;
    @EnumNamePattern(regexp = "AUTOMATIC|MANUAL")
    Transmission transmission;
    @EnumNamePattern(regexp = "FUEL|ELECTRIC|DIESEL")
    EngineType engineType;
}
