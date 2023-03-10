package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.category.CategoryCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.category.CategoryResponseDto;
import by.sergey.carrentapp.domain.dto.filterdto.CategoryFilter;
import by.sergey.carrentapp.domain.entity.Category;
import by.sergey.carrentapp.mapper.category.CategoryCreateMapper;
import by.sergey.carrentapp.mapper.category.CategoryResponseMapper;
import by.sergey.carrentapp.mapper.category.CategoryUpdateMapper;
import by.sergey.carrentapp.repository.CategoryRepository;
import by.sergey.carrentapp.service.exception.CategoryBadRequestException;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryCreateMapper categoryCreateMapper;
    private final CategoryResponseMapper categoryResponseMapper;
    private final CategoryUpdateMapper categoryUpdateMapper;

    @Transactional
    public Optional<CategoryResponseDto> create(CategoryCreateUpdateRequestDto categoryCreateUpdateRequestDto) {
        checkCategoryNameIsUnique(categoryCreateUpdateRequestDto.name());

        return Optional.of(categoryCreateMapper.mapToEntity(categoryCreateUpdateRequestDto))
                .map(categoryRepository::save)
                .map(categoryResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<CategoryResponseDto> update(Long id, CategoryCreateUpdateRequestDto categoryCreateUpdateRequestDto) {
        var existingCategory = getByIdOrElseThrow(id);

        if (!existingCategory.getName().equals(categoryCreateUpdateRequestDto.name())) {
            checkCategoryNameIsUnique(categoryCreateUpdateRequestDto.name());
        }

        return Optional.of(categoryUpdateMapper.mapToEntity(categoryCreateUpdateRequestDto, existingCategory))
                .map(categoryRepository::save)
                .map(categoryResponseMapper::mapToDto);
    }

    @Transactional
    public Boolean deleteById(long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<CategoryResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(categoryResponseMapper::mapToDto);
    }

    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryResponseMapper::mapToDto)
                .toList();
    }

    public List<CategoryResponseDto> getAllByPriceEquals(BigDecimal price) {
        return categoryRepository.findAllByPrice(price).stream()
                .map(categoryResponseMapper::mapToDto)
                .toList();
    }

    public List<CategoryResponseDto> getAllByPriceLess(BigDecimal price) {
        return categoryRepository.findAllByPriceLessThanEqual(price).stream()
                .map(categoryResponseMapper::mapToDto)
                .toList();
    }

    public List<CategoryResponseDto> getAllByPriceGreater(BigDecimal price) {
        return categoryRepository.findAllByPriceGreaterThanEqual(price).stream()
                .map(categoryResponseMapper::mapToDto)
                .toList();
    }

    public List<CategoryResponseDto> getAllByPrice(CategoryFilter categoryFilter) {
        var price = categoryFilter.getPrice();
        var type = categoryFilter.getType();
        List<CategoryResponseDto> categories;

        switch (type) {
            case "equals":
                categories = this.getAllByPriceEquals(price);
                break;
            case "greater":
                categories = this.getAllByPriceGreater(price);
                break;
            default:
                categories = this.getAllByPriceLess(price);
                break;
        }
        return categories;
    }

    private Category getByIdOrElseThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Category", "id", id)));
    }

    private void checkCategoryNameIsUnique(String categoryName) {
        if (categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new CategoryBadRequestException(ExceptionMessageUtil.getAlreadyExistsMessage("Category", "name", categoryName));
        }
    }
}
