package by.sergey.carrentapp.mapper.category;

import by.sergey.carrentapp.domain.dto.category.CategoryResponseDto;
import by.sergey.carrentapp.domain.entity.Category;
import by.sergey.carrentapp.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryResponseMapper implements ResponseMapper<Category, CategoryResponseDto> {
    @Override
    public CategoryResponseDto mapToDto(Category entity) {
        return CategoryResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .build();
    }
}
