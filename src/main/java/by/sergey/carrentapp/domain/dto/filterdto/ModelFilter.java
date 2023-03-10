package by.sergey.carrentapp.domain.dto.filterdto;

import by.sergey.carrentapp.domain.model.Transmission;
import by.sergey.carrentapp.domain.model.EngineType;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ModelFilter {
    List<String> brands;
    List<String> models;
    Transmission transmission;
    EngineType engineType;
}
