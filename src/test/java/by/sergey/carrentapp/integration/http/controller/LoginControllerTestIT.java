package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class LoginControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gmail.com";
    private final MockMvc mockMvc;
    private final UserService userService;
    private final HttpHeaders httpHeaders = new HttpHeaders();

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
    @Test
    void loginPage() throws Exception {
        var uriBuilder = fromUriString("/login");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/user/login"));

    }

    @Test
    void shouldLoginCorrectly() throws Exception {
        UriComponentsBuilder uriBuilder = fromUriString("/login");

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", "admin@gmail.com")
                                .param("password", "admin"))
                .andExpect(redirectedUrl("/cars"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void logout() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        var uriLoginBuilder = fromUriString("/login");

        mockMvc.perform(
                        post(uriLoginBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", userRequestDto.getEmail())
                                .param("password", userRequestDto.getPassword())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cars"));

        var uriLogoutBuilder = fromUriString("/logout");

        mockMvc.perform(
                        post(uriLogoutBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}