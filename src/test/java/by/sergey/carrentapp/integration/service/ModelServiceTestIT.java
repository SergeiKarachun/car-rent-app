package by.sergey.carrentapp.integration.service;


import by.sergey.carrentapp.domain.dto.model.ModelResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.BrandService;
import by.sergey.carrentapp.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.EXISTS_BRAND_ID;
import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.MODEL_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class ModelServiceTestIT extends IntegrationTestBase {

    public final ModelService modelService;
    public final BrandService brandService;

    @Test
    void create() {
        var brandDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());

        var actualModel = modelService.create(modelCreateRequestDto);

        assertTrue(actualModel.isPresent());
        assertEquals(actualModel.get().getName(), modelCreateRequestDto.getName());
        assertEquals(actualModel.get().getTransmission(), modelCreateRequestDto.getTransmission());
        assertEquals(actualModel.get().getEngineType(), modelCreateRequestDto.getEngineType());
    }

    @Test
    void update() {
        var brandDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);
        var modelUpdateRequestDto = TestDtoBuilder.createModelUpdateRequestDto();

        var updatedModel = modelService.update(savedModel.get().getId(), modelUpdateRequestDto);

        assertThat(updatedModel).isNotNull();
        updatedModel.ifPresent(actual -> assertEquals(modelUpdateRequestDto.getEngineType(), actual.getEngineType()));
        updatedModel.ifPresent(actual -> assertEquals(modelUpdateRequestDto.getName(), actual.getName()));
    }

    @Test
    void deleteById() {
        assertTrue(modelService.deleteById(MODEL_ID_FOR_DELETE));
    }

    @Test
    void getById() {
        var brandDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var expectedModel = modelService.create(modelCreateRequestDto);

        var actualModel = modelService.getById(expectedModel.get().getId());

        assertThat(actualModel).isNotNull();
        assertEquals(expectedModel, actualModel);
    }

    @Test
    void getAll() {
        var expectedModelsNames = List.of("A6", "A8", "525", "M5", "Rapid", "Superb", "Model S", "Model X");

        var actualModels = modelService.getAll();

        assertThat(actualModels).hasSize(8);
        var actualModelNames = actualModels.stream().map(ModelResponseDto::getName).collect(toList());
        assertThat(actualModelNames.containsAll(expectedModelsNames));
    }

    @Test
    void getAllByBrandId() {
        var expectedModelsNames = List.of("A6", "A8");

        var actualModels = modelService.getAllByBrandId(EXISTS_BRAND_ID);

        assertThat(actualModels).hasSize(2);
        var actualModelNames = actualModels.stream().map(ModelResponseDto::getName).collect(toList());
        assertThat(actualModelNames.containsAll(expectedModelsNames));
    }
}