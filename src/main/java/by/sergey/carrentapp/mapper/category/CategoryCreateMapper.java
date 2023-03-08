package by.sergey.carrentapp.mapper.category;

import by.sergey.carrentapp.domain.dto.category.CategoryCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Category;
import by.sergey.carrentapp.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryCreateMapper implements CreateMapper<CategoryCreateUpdateRequestDto, Category> {
    @Override
    public Category mapToEntity(CategoryCreateUpdateRequestDto requestDto) {
        return Category.builder()
                .name(requestDto.name())
                .price(requestDto.price())
                .build();
    }
}
