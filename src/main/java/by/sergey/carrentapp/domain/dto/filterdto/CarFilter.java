package by.sergey.carrentapp.domain.dto.filterdto;

import by.sergey.carrentapp.domain.model.Color;
import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CarFilter {
    Color color;
    Integer year;
    List<String> brandNames;
    List<String> modelNames;
    List<String> categoryNames;
    Transmission transmission;
    EngineType engineType;
}
