package by.sergey.carrentapp.domain.dto.brand;

import by.sergey.carrentapp.domain.dto.model.ModelResponseDto;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class BrandResponseDto {
    Long id;
    String name;
    Set<ModelResponseDto> models;
}
