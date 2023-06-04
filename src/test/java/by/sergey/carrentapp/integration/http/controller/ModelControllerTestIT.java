package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.domain.dto.model.ModelResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.BrandService;
import by.sergey.carrentapp.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static by.sergey.carrentapp.integration.http.controller.ModelControllerTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
class ModelControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gmail.com";
    static final String ENDPOINT = "/models";
    private final MockMvc mockMvc;
    private final ModelService modelService;
    private final BrandService brandService;
    private final HttpHeaders httpHeaders = new HttpHeaders();

    @Test
    void findAllModels() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/model/models"))
                .andExpect(model().attributeExists("models"))
                .andExpect(model().attributeExists("transmissions"))
                .andExpect(model().attributeExists("engines"))
                .andExpect(model().attributeExists("modelFilter"))
                .andReturn();

        var models = (List<ModelResponseDto>) result.getModelAndView().getModel().get("models");
        assertThat(models).hasSize(8);
    }

    @Test
    void create() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var savedBrand = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(savedBrand.get().getId());
        var uriBuilder = fromUriString(ENDPOINT);
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("brandId", modelCreateRequestDto.getBrandId().toString())
                                .param("name", modelCreateRequestDto.getName())
                                .param("transmission", modelCreateRequestDto.getTransmission().name())
                                .param("engineType", modelCreateRequestDto.getEngineType().name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("success_message", "You create new model successfully"));
    }

    @Test
    void createViewForCreatingModel() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/model-create");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("model"))
                .andExpect(model().attributeExists("transmissions"))
                .andExpect(model().attributeExists("engines"))
                .andExpect(model().attributeExists("brands"))
                .andExpect(view().name("layout/model/model-create"));
    }

    @Test
    void findById() throws Exception {
        var brandCreateDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var savedBrand = brandService.create(brandCreateDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(savedBrand.get().getId());
        var expectedModel = modelService.create(modelCreateRequestDto).get();

        assertExpectedIsSaved(expectedModel, expectedModel.getId());
    }

    @Test
    void update() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto);

        var expectedModel = savedModel.get();

        assertExpectedIsSaved(expectedModel, expectedModel.getId());

        var modelUpdateRequestDto = TestDtoBuilder.createModelUpdateRequestDto();
        var uriBuilder = fromUriString(ENDPOINT + "/" + expectedModel.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", modelUpdateRequestDto.getName())
                                .param("transmission", modelUpdateRequestDto.getTransmission().name())
                                .param("engineType", modelUpdateRequestDto.getEngineType().name())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + expectedModel.getId()));
    }

    @Test
    void delete() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandCreateRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var savedModel = modelService.create(modelCreateRequestDto).get();

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedModel.getId() + "/delete");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));
    }

    @Test
    void shouldReturn404OnDelete() throws Exception {
        mockMvc.perform(
                        post(fromUriString(ENDPOINT + "/456/delete").build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNotFound());
    }

    private void assertExpectedIsSaved(ModelResponseDto expectedModel, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("model", expectedModel))
                .andReturn();

        var actualModel = (ModelResponseDto) result.getModelAndView().getModel().get("model");

        assertThat(actualModel.getId()).isEqualTo(id);
        assertThat(actualModel.getName()).isEqualTo(expectedModel.getName());
        assertThat(actualModel.getTransmission()).isEqualTo(expectedModel.getTransmission());
        assertThat(actualModel.getEngineType()).isEqualTo(expectedModel.getEngineType());
        assertThat(actualModel.getBrand()).isEqualTo(expectedModel.getBrand());
    }
}