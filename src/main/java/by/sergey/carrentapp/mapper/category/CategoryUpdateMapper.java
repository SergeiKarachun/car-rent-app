package by.sergey.carrentapp.mapper.category;

import by.sergey.carrentapp.domain.dto.category.CategoryCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Category;
import by.sergey.carrentapp.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryUpdateMapper implements UpdateMapper<CategoryCreateUpdateRequestDto, Category> {
    @Override
    public Category mapToEntity(CategoryCreateUpdateRequestDto dto, Category entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(CategoryCreateUpdateRequestDto dto, Category entity) {
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
    }
}
