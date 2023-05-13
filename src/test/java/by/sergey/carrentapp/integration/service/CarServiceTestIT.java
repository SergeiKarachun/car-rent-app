package by.sergey.carrentapp.integration.service;

import by.sergey.carrentapp.domain.dto.car.CarResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.BrandService;
import by.sergey.carrentapp.service.CarService;
import by.sergey.carrentapp.service.CategoryService;
import by.sergey.carrentapp.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.CAR_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@RequiredArgsConstructor
class CarServiceTestIT extends IntegrationTestBase {

    private final BrandService brandService;
    private final ModelService modelService;
    private final CategoryService categoryService;
    private final CarService carService;

    @Test
    void create() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());

        var actualCar = carService.create(carRequestDto);

        assertThat(actualCar).isNotNull();
        assertThat(actualCar.get().getId()).isNotNull();
    }

    @Test
    void update() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());
        var savedCar = carService.create(carRequestDto);
        var carUpdateRequestDto = TestDtoBuilder.updateCarRequestDto(carRequestDto.getModelId());

        var updatedCar = carService.update(savedCar.get().getId(), carUpdateRequestDto);

        assertThat(updatedCar).isNotNull();
        assertEquals(savedCar.get().getId(), updatedCar.get().getId());
        assertEquals(carUpdateRequestDto.getYear(), updatedCar.get().getYear());
        assertEquals(carUpdateRequestDto.getColor(), updatedCar.get().getColor());
        assertEquals(carUpdateRequestDto.getCarNumber(), updatedCar.get().getCarNumber());
    }

    @Test
    void deleteById() {
        assertThat(carService.deleteById(CAR_ID_FOR_DELETE));
    }

    @Test
    void getById() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());
        var expectedCar = carService.create(carRequestDto);

        var actualCar = carService.getById(expectedCar.get().getId());

        assertThat(actualCar).isNotNull();
        actualCar.ifPresent(actual -> assertEquals(expectedCar.get(), actual));
    }

    @Test
    void getAll() {
        var expectedModelsNames = List.of("A6", "A8", "525", "M5", "Rapid", "Superb", "Model S", "Model X");

        var actualCars = carService.getAll();
        var actualModelNames = actualCars.stream().map(CarResponseDto::getModel).collect(toList());

        assertThat(actualModelNames).hasSize(8);
        assertThat(actualModelNames.containsAll(expectedModelsNames));
    }

    @Test
    void getByCarNumber() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());
        var expectedCar = carService.create(carRequestDto);

        var actualCar = carService.getByCarNumber(expectedCar.get().getCarNumber());

        assertThat(actualCar).isNotNull();
        actualCar.ifPresent(actual -> assertEquals(expectedCar.get(), actual));
    }

    @Test
    void getAllWithAccidents() {
        var allWithAccidents = carService.getAllWithAccidents(0, 10);

        assertThat(allWithAccidents).hasSize(2);
    }

    @Test
    void getAllWithoutAccidents() {
        var allWithAccidents = carService.getAllWithoutAccidents(0, 10);

        assertThat(allWithAccidents).hasSize(7);
    }

    @Test
    void getAllRepaired() {
        var allWithAccidents = carService.getAllRepaired();

        assertThat(allWithAccidents).hasSize(8);
    }

    @Test
    void getAllBroken() {
        var allWithAccidents = carService.getAllBroken();

        assertThat(allWithAccidents).hasSize(0);
    }
}