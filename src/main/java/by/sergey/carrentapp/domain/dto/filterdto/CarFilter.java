package by.sergey.carrentapp.domain.dto.filterdto;

import by.sergey.carrentapp.domain.model.Color;
import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class CarFilter {
    Color color;
    Integer year;
    String brandName;
    String modelName;
    String categoryName;
    Transmission transmission;
    EngineType engineType;
}
