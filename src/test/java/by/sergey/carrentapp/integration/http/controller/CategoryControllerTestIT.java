package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.domain.dto.category.CategoryCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.category.CategoryResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.CategoryService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static by.sergey.carrentapp.integration.http.controller.CategoryControllerTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
class CategoryControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gmail.com";
    static final String ENDPOINT = "/categories";
    private final MockMvc mockMvc;
    private final CategoryService categoryService;
    private final HttpHeaders httpHeaders = new HttpHeaders();

    @Test
    void getAll() throws Exception {
        var result = mockMvc.perform(get(fromUriString(ENDPOINT).build().encode().toUri())
                        .headers(httpHeaders)
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/category/categories"))
                .andExpect(model().attributeExists("categoryFilter"))
                .andExpect(model().attributeExists("categories"))
                .andReturn();

        var categories = (List<CategoryResponseDto>) result.getModelAndView().getModel().get("categories");

        assertThat(categories).hasSize(5);
    }

    @Test
    void getById() throws Exception {
        var categoryCreateUpdateRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var savedCategory = categoryService.create(categoryCreateUpdateRequestDto);
        var expected = savedCategory.get();

        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    void update() throws Exception {
        var categoryCreateUpdateRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var savedCategory = categoryService.create(categoryCreateUpdateRequestDto).get();

        assertExpectedIsSaved(savedCategory, savedCategory.getId());

        var categoryToUpdate = new CategoryCreateUpdateRequestDto("VIP+", BigDecimal.valueOf(350));

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedCategory.getId() + "/update");

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", categoryToUpdate.getName())
                                .param("price", categoryToUpdate.getPrice().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + savedCategory.getId()));
    }

    @Test
    void createView() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/category-create");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/category/category-create"))
                .andExpect(model().attributeExists("category"));
    }

    @Test
    void createCategory() throws Exception {
        var categoryCreateUpdateRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();

        var uriBuilder = fromUriString(ENDPOINT);

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", categoryCreateUpdateRequestDto.getName())
                                .param("price", categoryCreateUpdateRequestDto.getPrice().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("success_message", "You successfully create new category"));
    }

    @Test
    void delete() throws Exception {
        var categoryCreateUpdateRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var savedCategory = categoryService.create(categoryCreateUpdateRequestDto);

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedCategory.get().getId() + "/delete");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> categoryService.getById(savedCategory.get().getId()));

        assertEquals("404 NOT_FOUND \"Category with id 6 does not exist.\"", result.getMessage());
    }

    @Test
    void shouldReturn404OnDelete() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/12234/delete");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void findByName() throws Exception {
        var categoryCreateUpdateRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var savedCategory = categoryService.create(categoryCreateUpdateRequestDto).get();

        assertExpectedIsSaved(savedCategory, savedCategory.getId());

        var uriBuilder = fromUriString(ENDPOINT + "/by-name");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", savedCategory.getName())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + savedCategory.getId()));
    }

    @Test
    void findAllByFilter() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/filter");

        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("price", String.valueOf(120))
                                .param("type", String.valueOf("greater"))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/category/categories"))
                .andExpect(model().attributeExists("categories"))
                .andReturn();

        var priceLs = (List<CategoryResponseDto>) result.getModelAndView().getModel().get("categories");
        assertThat(priceLs).hasSize(3);
    }

    private void assertExpectedIsSaved(CategoryResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("category", expected))
                .andReturn();

        var category = (CategoryResponseDto) result.getModelAndView().getModel().get("category");

        assertThat(category.getName()).isEqualTo(expected.getName());
        assertThat(category.getPrice()).isEqualTo(expected.getPrice());
        assertThat(category.getId()).isEqualTo(id);
    }
}