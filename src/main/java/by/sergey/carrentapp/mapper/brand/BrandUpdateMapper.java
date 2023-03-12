package by.sergey.carrentapp.mapper.brand;

import by.sergey.carrentapp.domain.dto.brand.BrandCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class BrandUpdateMapper implements UpdateMapper<BrandCreateUpdateRequestDto, Brand> {
    @Override
    public Brand mapToEntity(BrandCreateUpdateRequestDto dto, Brand entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(BrandCreateUpdateRequestDto dto, Brand entity) {
        entity.setName(dto.getName());
    }
}
