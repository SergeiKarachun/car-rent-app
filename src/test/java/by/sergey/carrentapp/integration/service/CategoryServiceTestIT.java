package by.sergey.carrentapp.integration.service;

import by.sergey.carrentapp.domain.dto.category.CategoryCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.category.CategoryResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class CategoryServiceTestIT extends IntegrationTestBase {

    private final CategoryService categoryService;

    @Test
    void create() {
        var categoryDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var actualCategory = categoryService.create(categoryDto);

        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.get().getId()).isNotNull();
    }

    @Test
    void update() {
        var categoryDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var createdCategory = categoryService.create(categoryDto);
        var categoryToUpdate = new CategoryCreateUpdateRequestDto("HUMBLE", BigDecimal.valueOf(30));

        var actualCategory = categoryService.update(createdCategory.get().getId(), categoryToUpdate);

        assertThat(actualCategory).isNotNull();
        actualCategory.ifPresent(actual -> assertEquals(categoryToUpdate.getName(), actual.getName()));
    }

    @Test
    void deleteById() {
        var categoryDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var expectedCategory = categoryService.create(categoryDto);

        var actualCategory = categoryService.getById(expectedCategory.get().getId());

        assertThat(actualCategory).isNotNull();
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void findByName() {
        var categoryName = new String("StAnDarT");

        var actualCategory = categoryService.findByName(categoryName);

        assertThat(actualCategory).isNotNull();
        actualCategory.ifPresent(actual -> assertEquals(categoryName.toUpperCase(), actual.getName()));
    }

    @Test
    void getById() {
        var categoryDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var expectedCategory = categoryService.create(categoryDto);

        var actualCategory = categoryService.getById(expectedCategory.get().getId());

        assertThat(actualCategory).isNotNull();
        actualCategory.ifPresent(actual -> assertEquals(expectedCategory.get(), actual));
    }

    @Test
    void getAll() {
        var expectedCategoryNames = List.of("ECONOMY", "SPORT", "STANDART", "LUXURY", "BUSINESS");

        var actualCategories = categoryService.getAll();

        assertThat(actualCategories).hasSize(5);
        var actualCategoryNames = actualCategories.stream().map(CategoryResponseDto::getName).collect(toList());
        assertTrue(actualCategoryNames.containsAll(expectedCategoryNames));
    }

    @Test
    void getAllByPriceEquals() {
        var categoryCreateUpdateRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var expectedCategory = categoryService.create(categoryCreateUpdateRequestDto);

        var actualCategory = categoryService.getAllByPriceEquals(expectedCategory.get().getPrice());

        assertThat(actualCategory).hasSize(1);
        assertEquals(expectedCategory.get(), actualCategory.get(0));
    }

    @Test
    void getAllByPriceLess() {
        var expectedCategoryNames = List.of("ECONOMY", "SPORT", "STANDART");

        var actualCategories = categoryService.getAllByPriceLess(BigDecimal.valueOf(120));

        assertThat(actualCategories).hasSize(3);
        var actualCategoryNames = actualCategories.stream().map(CategoryResponseDto::getName).collect(toList());
        assertTrue(actualCategoryNames.containsAll(expectedCategoryNames));
    }

    @Test
    void getAllByPriceGreater() {
        var expectedCategoryNames = List.of("LUXURY", "BUSINESS");

        var actualCategories = categoryService.getAllByPriceGreater(BigDecimal.valueOf(150));

        assertThat(actualCategories).hasSize(2);
        var actualCategoryNames = actualCategories.stream().map(CategoryResponseDto::getName).collect(toList());
        assertTrue(actualCategoryNames.containsAll(expectedCategoryNames));
    }
}