package by.sergey.carrentapp.integration.service;

import by.sergey.carrentapp.domain.dto.brand.BrandCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.brand.BrandResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.TestEntityIdConst;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class BrandServiceTestIT extends IntegrationTestBase {

    private final BrandService brandService;

    @Test
    void create() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        Optional<BrandResponseDto> actualBrand = brandService.create(brandCreateRequestDto);

        assertTrue(actualBrand.isPresent());
        assertEquals(brandCreateRequestDto.getName(), actualBrand.get().getName());
    }

    @Test
    void update() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandToUpdateRequestDto = new BrandCreateUpdateRequestDto("Honda Corp.");

        var savedBrand = brandService.create(brandCreateRequestDto);
        var updatedBrand = brandService.update(savedBrand.get().getId(), brandToUpdateRequestDto);

        assertThat(updatedBrand).isNotNull();
        updatedBrand.ifPresent(brand -> assertEquals(brand.getName(), brandToUpdateRequestDto.getName()));
    }

    @Test
    void getById() {
        var brandCreateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var expectedBrand = brandService.create(brandCreateRequestDto);

        var actualBrand = brandService.getById(expectedBrand.get().getId());

        assertThat(actualBrand).isNotNull();
        assertEquals(expectedBrand, actualBrand);
    }

    @Test
    void getAll() {
        List<BrandResponseDto> actualBrands = brandService.getAll();

        assertThat(actualBrands).hasSize(4);

        List<String> names = actualBrands.stream().map(BrandResponseDto::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("Audi", "BMW", "Skoda", "Tesla");
    }

    @Test
    void getByName() {
        var brandCreateDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var expectedBrandDto = brandService.create(brandCreateDto);

        var actualBrandDto = brandService.getById(expectedBrandDto.get().getId());

        assertThat(actualBrandDto).isNotNull();
        actualBrandDto.ifPresent(actual -> assertEquals(expectedBrandDto, actualBrandDto));
    }

    @Test
    void getByNames() {
        var expectedNames = List.of("Audi", "BMW");

        List<BrandResponseDto> actual = brandService.getByNames(expectedNames);

        assertThat(actual).hasSize(2);
        List<String> actualNames = actual.stream().map(BrandResponseDto::getName).collect(toList());
        assertThat(actualNames).containsExactlyInAnyOrder(expectedNames.get(0), expectedNames.get(1));
    }

    @Test
    void deleteById() {
        assertTrue(brandService.deleteById(TestEntityIdConst.BRAND_ID_FOR_DELETE));
    }
}