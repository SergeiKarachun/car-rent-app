package by.sergey.carrentapp.domain.dto.filterdto;

import by.sergey.carrentapp.domain.model.Transmission;
import by.sergey.carrentapp.domain.model.EngineType;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class ModelFilter {
    String brandName;
    String modelName;
    Transmission transmission;
    EngineType engineType;
}
