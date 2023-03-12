package by.sergey.carrentapp.mapper.brand;

import by.sergey.carrentapp.domain.dto.brand.BrandCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.mapper.CreateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandCreateMapper implements CreateMapper<BrandCreateUpdateRequestDto, Brand> {
    @Override
    public Brand mapToEntity(BrandCreateUpdateRequestDto requestDto) {

        return Brand.builder()
                .name(requestDto.getName())
                .build();
    }
}
