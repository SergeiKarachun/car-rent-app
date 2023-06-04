package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.UserDetailsService;
import by.sergey.carrentapp.service.UserService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static by.sergey.carrentapp.integration.http.controller.UserDetailsControllerTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
class UserDetailsControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gamil.com";
    static final String ENDPOINT = "/user-details";
    private final MockMvc mockMvc;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final UserService userService;
    private final UserDetailsService userDetailsService;

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
                .andExpect(view().name("layout/user/user-details"))
                .andExpect(model().attributeExists("usersDetailsPage"))
                .andExpect(model().attributeExists("filter"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("size"))
                .andReturn();

        var usersDetailsPage = ((Page<UserDetailsResponseDto>) result.getModelAndView().getModel().get("usersDetailsPage")).getContent();
        assertThat(usersDetailsPage).hasSize(2);
    }

    @Test
    void getById() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();
        var expectedUserDetails = savedUser.getUserDetailsDto();

        assertExpectedIsSaved(expectedUserDetails, expectedUserDetails.getId());
    }

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = "ADMIN")
    @Test
    void update() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();
        var expectedUserDetails = savedUser.getUserDetailsDto();

        assertExpectedIsSaved(expectedUserDetails, expectedUserDetails.getId());

        var userDetailsUpdateRequestDto = TestDtoBuilder.createUserDetailsUpdateRequestDto();
        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId() + "/update");

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", userDetailsUpdateRequestDto.getName())
                                .param("surname", userDetailsUpdateRequestDto.getSurname())
                                .param("address", userDetailsUpdateRequestDto.getAddress())
                                .param("phone", userDetailsUpdateRequestDto.getPhone())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-details/" + savedUser.getId()));
    }

    @WithMockCustomUser(username = MOCK_USERNAME, email = "client@gmail.com", password = "client", authorities = "CLIENT")
    @Test
    void updateForClient() throws Exception {
        var userDetailsUpdateRequestDto = TestDtoBuilder.createUserDetailsUpdateRequestDto();
        var uriBuilder = fromUriString(ENDPOINT + "/" + "1" + "/update");

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", userDetailsUpdateRequestDto.getName())
                                .param("surname", userDetailsUpdateRequestDto.getSurname())
                                .param("address", userDetailsUpdateRequestDto.getAddress())
                                .param("phone", userDetailsUpdateRequestDto.getPhone())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/users/profile/{\\d+}"));
    }

    @Test
    void delete() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();
        var expectedUserDetails = savedUser.getUserDetailsDto();

        assertExpectedIsSaved(expectedUserDetails, expectedUserDetails.getId());

        var uriBuilder = fromUriString(ENDPOINT + "/" + expectedUserDetails.getId() + "/delete");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> userDetailsService.getById(expectedUserDetails.getId()));
        assertEquals("404 NOT_FOUND \"UserDetails with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    void getByUserId() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();
        var expectedUserDetails = savedUser.getUserDetailsDto();

        var uriBuilder = fromUriString(ENDPOINT + "/by-user-id");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("id", savedUser.getId().toString())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("userDetails"))
                .andReturn();

        var userDetails = (UserDetailsResponseDto) result.getModelAndView().getModel().get("userDetails");

        assertThat(userDetails.getId()).isEqualTo(expectedUserDetails.getId());
        assertThat(userDetails.getName()).isEqualTo(expectedUserDetails.getName());
        assertThat(userDetails.getSurname()).isEqualTo(expectedUserDetails.getSurname());
        assertThat(userDetails.getAddress()).isEqualTo(expectedUserDetails.getAddress());
        assertThat(userDetails.getPhone()).isEqualTo(expectedUserDetails.getPhone());
        assertThat(userDetails.getBirthday()).isEqualTo(expectedUserDetails.getBirthday());
    }

    @Test
    void getByNameAndSurname() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();
        var expectedUserDetails = savedUser.getUserDetailsDto();

        var uriBuilder = fromUriString(ENDPOINT + "/by-name-surname");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("name", expectedUserDetails.getName())
                                .param("surname", expectedUserDetails.getSurname())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("usersDetailsPage"))
                .andExpect(model().attributeExists("filter"))
                .andExpect(model().attributeExists("size"))
                .andExpect(model().attributeExists("page"))
                .andExpect(view().name("layout/user/user-details"))
                .andReturn();

        var userDetailsPage = ((Page<UserDetailsResponseDto>) result.getModelAndView().getModel().get("usersDetailsPage")).getContent();

        assertThat(userDetailsPage).hasSize(1);
    }

    @Test
    void getByRegistrationDates() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/by-registration-dates");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("from", LocalDate.now().toString())
                                .param("to", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usersDetailsPage"))
                .andReturn();
    }

    private void assertExpectedIsSaved(UserDetailsResponseDto expectedUserDetails, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("userDetails"))
                .andReturn();

        var userDetails = (UserDetailsResponseDto) result.getModelAndView().getModel().get("userDetails");

        assertThat(userDetails.getId()).isEqualTo(expectedUserDetails.getId());
        assertThat(userDetails.getName()).isEqualTo(expectedUserDetails.getName());
        assertThat(userDetails.getSurname()).isEqualTo(expectedUserDetails.getSurname());
        assertThat(userDetails.getAddress()).isEqualTo(expectedUserDetails.getAddress());
        assertThat(userDetails.getPhone()).isEqualTo(expectedUserDetails.getPhone());
        assertThat(userDetails.getBirthday()).isEqualTo(expectedUserDetails.getBirthday());
    }
}