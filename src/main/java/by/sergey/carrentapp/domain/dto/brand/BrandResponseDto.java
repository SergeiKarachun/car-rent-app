package by.sergey.carrentapp.domain.dto.brand;

import by.sergey.carrentapp.domain.dto.model.ModelResponseDto;
import lombok.Builder;

import java.util.Set;

@Builder
public record BrandResponseDto(Long id,
                               String name,
                               Set<ModelResponseDto> models) {
}
