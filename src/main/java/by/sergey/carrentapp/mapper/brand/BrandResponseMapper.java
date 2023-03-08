package by.sergey.carrentapp.mapper.brand;

import by.sergey.carrentapp.domain.dto.brand.BrandResponseDto;
import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.mapper.ResponseMapper;
import by.sergey.carrentapp.mapper.model.ModelResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandResponseMapper implements ResponseMapper<Brand, BrandResponseDto> {

    private final ModelResponseMapper modelResponseMapper;
    @Override
    public BrandResponseDto mapToDto(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .models(modelResponseMapper.mapToDtos(brand.getModels()))
                .build();
    }
}
