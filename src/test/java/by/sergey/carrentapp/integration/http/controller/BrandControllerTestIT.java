package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.domain.dto.brand.BrandCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.brand.BrandResponseDto;
import by.sergey.carrentapp.domain.projection.BrandFullView;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.BrandService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.stream.Collectors;

import static by.sergey.carrentapp.domain.dto.brand.BrandCreateUpdateRequestDto.Fields.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = BrandControllerTestIT.MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
class BrandControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gmail.com";
    static final String ENDPOINT = "/brands";

    private final MockMvc mockMvc;
    private final BrandService brandService;
    private final HttpHeaders httpHeaders = new HttpHeaders();

    @Test
    void findAllBrands() throws Exception {
        var result = mockMvc.perform(get("/brands")
                        .headers(httpHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/brand/brands"))
                .andExpect(model().attributeExists("brands"))
                .andReturn();

        var brands = ((List<BrandResponseDto>) result.getModelAndView().getModel().get("brands"));
        assertThat(brands).hasSize(4);
    }

    @Test
    void shouldReturnNotFoundForInvalidEndpoint() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/54065615");

        mockMvc.perform(get(uriBuilder.build().encode().toUri())
                        .headers(httpHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void findById() throws Exception {
        var createRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var saved = brandService.create(createRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    void findByName() throws Exception {
        var createRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var expected = brandService.create(createRequestDto);

        var uriBuilder = fromUriString(ENDPOINT + "/by-name");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param(name, "Honda"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/brands/" + expected.get().getId()))
                .andReturn();
    }

    @Test
    void findByNames() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/by-names");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", "Audi,Tesla")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("brands"))
                .andExpect(view().name("layout/brand/brands"))
                .andReturn();

        var brands = (List<BrandResponseDto>) result.getModelAndView().getModel().get("brands");

        assertThat(brands).hasSize(2);
        var listOfNames = brands.stream()
                .map(BrandResponseDto::getName)
                .collect(Collectors.toList());
        assertThat(listOfNames).containsExactlyInAnyOrder("Audi", "Tesla");
    }

    @Test
    void findAllFullViewByName() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/all-by-names");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", "Audi")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/brand/brands-full-view"))
                .andExpect(model().attributeExists("listfullview"))
                .andReturn();

        var brands = (List<BrandFullView>) result.getModelAndView().getModel().get("listfullview");

        assertThat(brands).hasSize(2);
        var listOfNames = brands.stream()
                .map(BrandFullView::getModelName)
                .collect(Collectors.toList());
        assertThat(listOfNames).containsExactlyInAnyOrder("A6", "A8");
    }

    @Test
    void allBrandsWithFullView() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/brands-full-view");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/brand/brands-full-view"))
                .andExpect(model().attributeExists("listfullview"))
                .andReturn();

        var brands = (List<BrandFullView>) result.getModelAndView().getModel().get("listfullview");

        assertThat(brands).hasSize(8);
        var listOfNames = brands.stream()
                .map(BrandFullView::getModelName)
                .collect(Collectors.toList());
        assertThat(listOfNames).containsExactlyInAnyOrder("A6", "A8", "525", "M5", "Rapid", "Superb", "Model S", "Model X");
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/brands")
                        .headers(httpHeaders)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param(name, "Ford")
                )
                .andExpectAll(status().is3xxRedirection(),
                        model().attribute("success_message", "You create new brand successfully")
                );
    }

    @Test
    void createView() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/brand-create");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("brand"));
    }

    @Test
    void update() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var saved = brandService.create(brandCreateRequestDto);
        var expected = saved.get();

        assertExpectedIsSaved(expected, expected.getId());

        var brandUpdateRequestDto = new BrandCreateUpdateRequestDto("Honda-Corp");
        var uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", brandUpdateRequestDto.getName())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + expected.getId()));
    }

    @Test
    void delete() throws Exception {
        var brandCreateRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var saved = brandService.create(brandCreateRequestDto);

        mockMvc.perform(
                        post(fromUriString(ENDPOINT + "/" + saved.get().getId() + "/delete").build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> brandService.getById(saved.get().getId()));

        assertEquals("404 NOT_FOUND \"Brand with id 5 does not exist.\"", result.getMessage());
    }

    @Test
    void return404OnDelete() throws Exception {
        mockMvc.perform(
                        post(fromUriString(ENDPOINT + "/8789/delete").build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().isNotFound());
    }

    private void assertExpectedIsSaved(BrandResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("brand", expected))
                .andReturn();

        var responseDto = (BrandResponseDto) result.getModelAndView().getModel().get("brand");

        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getName()).isEqualTo(expected.getName());
    }
}