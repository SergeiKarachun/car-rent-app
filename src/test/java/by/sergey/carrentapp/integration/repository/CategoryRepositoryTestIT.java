package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.Category;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.TestEntityIdConst;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@RequiredArgsConstructor
class CategoryRepositoryTestIT extends IntegrationTestBase {

    private final CategoryRepository categoryRepository;

    @Test
    void createCategory() {
        Category categoryToSave = TestEntityBuilder.createCategory();
        Category savedCategory = categoryRepository.saveAndFlush(categoryToSave);

        assertThat(savedCategory.getId()).isNotNull();
    }

    @Test
    void updateCategory() {
        Category categoryToUpdate = categoryRepository.findById(TestEntityIdConst.EXISTS_CATEGORY_ID).get();
        categoryToUpdate.setName("EXCLUSIVE");
        categoryToUpdate.setPrice(BigDecimal.valueOf(888));

        categoryRepository.saveAndFlush(categoryToUpdate);

        Category updatedCategory = categoryRepository.findById(categoryToUpdate.getId()).get();

        assertThat(updatedCategory).isEqualTo(categoryToUpdate);
    }

    @Test
    void deleteCategory() {
        Optional<Category> categoryToDelete = categoryRepository.findById(TestEntityIdConst.CATEGORY_ID_FOR_DELETE);

        categoryToDelete.ifPresent(category -> categoryRepository.delete(category));

        assertThat(categoryRepository.findById(TestEntityIdConst.CATEGORY_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void findById() {
        Optional<Category> actualCategory = categoryRepository.findById(TestEntityIdConst.EXISTS_CATEGORY_ID);

        Optional<Category> existCategory = Optional.of(ExistsEntityBuilder.getExistCategory());

        assertThat(actualCategory).isNotNull();
        assertEquals(existCategory, actualCategory);
    }

    @Test
    void findByNameIgnoreCase() {
        Optional<Category> existCategory = Optional.of(ExistsEntityBuilder.getExistCategory());
        Optional<Category> actual = categoryRepository.findByNameIgnoreCase("econOmy");

        assertEquals(existCategory, actual);
    }

    @Test
    void findAllByPrice() {
        List<Category> actual = categoryRepository.findAllByPrice(BigDecimal.valueOf(50));
        Category existCategory = ExistsEntityBuilder.getExistCategory();

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(existCategory);
    }

    @Test
    void findAllByPriceLessThanEqual() {
        List<Category> allByPriceLessThanEqual = categoryRepository.findAllByPriceLessThanEqual(BigDecimal.valueOf(150));

        List<String> collect = allByPriceLessThanEqual.stream().map(cat -> cat.getName()).collect(Collectors.toList());

        assertThat(collect).hasSize(4).containsExactlyInAnyOrder("ECONOMY", "BUSINESS", "SPORT", "STANDART");
    }

    @Test
    void findAllByPriceGreaterThan() {
        List<Category> allByPriceGreaterThan = categoryRepository.findAllByPriceGreaterThanEqual(BigDecimal.valueOf(120));

        List<String> collect = allByPriceGreaterThan.stream().map(cat -> cat.getName()).collect(Collectors.toList());

        assertThat(collect).hasSize(3).containsExactlyInAnyOrder( "SPORT", "BUSINESS", "LUXURY");
    }

    @Test
    void existsByNameIgnoreCase() {
        boolean exist = categoryRepository.existsByNameIgnoreCase(ExistsEntityBuilder.getExistCategory().getName());
        assertThat(exist).isTrue();
    }
}