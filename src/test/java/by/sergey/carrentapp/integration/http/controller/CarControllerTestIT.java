package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.domain.dto.car.CarCreateRequestDto;
import by.sergey.carrentapp.domain.dto.car.CarResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.BrandService;
import by.sergey.carrentapp.service.CarService;
import by.sergey.carrentapp.service.CategoryService;
import by.sergey.carrentapp.service.ModelService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static by.sergey.carrentapp.integration.http.controller.CarControllerTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
class CarControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gmail.com";
    static final String ENDPOINT = "/cars";
    private final CarService carService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final CategoryService categoryService;
    private final MockMvc mockMvc;
    private final HttpHeaders httpHeaders = new HttpHeaders();

    @Test
    void getAll() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/car/cars"))
                .andExpectAll(model().attributeExists("totalPages", "carPage", "brands", "categories", "transmissions"))
                .andReturn();

        var carPage = ((Page<CarResponseDto>) result.getModelAndView().getModel().get("carPage")).getContent();
        assertThat(carPage).hasSize(8);
    }

    @Test
    void findAllWithAccidents() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/with-accidents");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/car/cars"))
                .andExpectAll(model().attributeExists("totalPagesWith", "carPage", "page", "size"))
                .andReturn();

        var carPage = ((Page<CarResponseDto>) result.getModelAndView().getModel().get("carPage")).getContent();
        assertThat(carPage).hasSize(2);
    }

    @Test
    void findAllWithoutAccidents() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/without-accidents");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/car/cars"))
                .andExpectAll(model().attributeExists("totalPagesWithout", "carPage", "page", "size"))
                .andReturn();

        var carPage = ((Page<CarResponseDto>) result.getModelAndView().getModel().get("carPage")).getContent();
        assertThat(carPage).hasSize(7);
    }

    @Test
    void getById() throws Exception {
        var car = getCarRequestDto();
        var expected = carService.create(car).get();

        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    void getByNumber() throws Exception {
        var car = getCarRequestDto();
        var savedCar = carService.create(car).get();

        var uriBuilder = fromUriString(ENDPOINT + "/by-number");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("number", savedCar.getCarNumber())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/car/car"))
                .andExpect(model().attributeExists("car"))
                .andReturn();

        var actualCar = (CarResponseDto) result.getModelAndView().getModel().get("car");

        assertThat(actualCar.getCarNumber()).isEqualTo(savedCar.getCarNumber());
        assertThat(actualCar.getId()).isEqualTo(savedCar.getId());
    }

    @Test
    void createView() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/car-create");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/car/car-create"));
    }

    @Test
    void createCar() throws Exception {
        var carRequestDto = getCarRequestDto();

        var uriBuilder = fromUriString(ENDPOINT);

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("brandId", carRequestDto.getBrandId().toString())
                                .param("modelId", carRequestDto.getModelId().toString())
                                .param("categoryId", carRequestDto.getCategoryId().toString())
                                .param("color", carRequestDto.getColor().name())
                                .param("year", carRequestDto.getYear().toString())
                                .param("carNumber", carRequestDto.getCarNumber())
                                .param("vin", carRequestDto.getVin())
                                .param("isRepaired", carRequestDto.getIsRepaired().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("success_message", "New car created successfully"));
    }

    @Test
    void update() throws Exception {
        var carRequestDto = getCarRequestDto();
        var savedCar = carService.create(carRequestDto).get();
        var carUpdateRequestDto = TestDtoBuilder.updateCarRequestDto(savedCar.getId());

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedCar.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("modelId", carUpdateRequestDto.getModelId().toString())
                                .param("categoryId", carUpdateRequestDto.getCategoryId().toString())
                                .param("year", carUpdateRequestDto.getYear().toString())
                                .param("carNumber", carUpdateRequestDto.getCarNumber())
                                .param("color", carUpdateRequestDto.getColor().name())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + savedCar.getId()));
    }

    @Test
    void delete() throws Exception {
        var carRequestDto = getCarRequestDto();
        var savedCar = carService.create(carRequestDto).get();

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedCar.getId() + "/delete");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> carService.getById(savedCar.getId()));

        assertEquals("404 NOT_FOUND \"Car with id 9 does not exist.\"", result.getMessage());
    }

    private CarCreateRequestDto getCarRequestDto() {
        var brandUpdateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandUpdateRequestDto).get();
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto).get();
        var categoryCreateUpdateRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryCreateUpdateRequestDto).get();
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.getId(), modelResponseDto.getId(), categoryResponseDto.getId());
        return carRequestDto;
    }

    private void assertExpectedIsSaved(CarResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("car"))
                .andReturn();

        var car = (CarResponseDto) result.getModelAndView().getModel().get("car");

        assertThat(car.getId()).isEqualTo(id);
        assertThat(car.getBrand()).isEqualTo(expected.getBrand());
        assertThat(car.getModel()).isEqualTo(expected.getModel());
        assertThat(car.getCarNumber()).isEqualTo(expected.getCarNumber());
        assertThat(car.getCategory()).isEqualTo(expected.getCategory());
        assertThat(car.getTransmission().name()).isEqualTo(expected.getTransmission().name());
        assertThat(car.getVin()).isEqualTo(expected.getVin());
        assertThat(car.getColor()).isEqualTo(expected.getColor());
        assertThat(car.getYear()).isEqualTo(expected.getYear());
        assertThat(car.getPrice()).isEqualTo(expected.getPrice());
    }
}