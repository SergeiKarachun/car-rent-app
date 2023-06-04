package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.domain.dto.user.UserResponseDto;
import by.sergey.carrentapp.domain.model.Role;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.UserService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import static by.sergey.carrentapp.integration.http.controller.UserControllerTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
class UserControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gmail.com";
    static final String ENDPOINT = "/users";
    private final UserService userService;
    private final MockMvc mockMvc;
    private HttpHeaders httpHeaders = new HttpHeaders();

    @Test
    void getAllUsers() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);

        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .contentType(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("usersPage"))
                .andReturn();

        var usersPage = ((Page<UserResponseDto>) result.getModelAndView().getModel().get("usersPage")).getContent();

        assertThat(usersPage).hasSize(2);
    }

    @Test
    void getById() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var expectedUser = userService.create(userRequestDto).get();

        assertExpectedIsSaved(expectedUser, expectedUser.getId());
    }

    @Test
    void getProfileById() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var expectedUser = userService.create(userRequestDto).get();

        var uriBuilder = fromUriString(ENDPOINT + "/profile/" + expectedUser.getId());

        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/user/profile"))
                .andExpect(model().attributeExists("user"))
                .andReturn();

        var user = (UserResponseDto) result.getModelAndView().getModel().get("user");

        assertThat(user.getId()).isEqualTo(expectedUser.getId());
        assertThat(user.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(user.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(user.getRole()).isEqualTo(expectedUser.getRole());
        assertThat(user.getUserDetailsDto().getId()).isEqualTo(expectedUser.getUserDetailsDto().getId());
        assertThat(user.getDriverLicenseDto().getId()).isEqualTo(expectedUser.getDriverLicenseDto().getId());
    }

    @Test
    void createView() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/sign-up");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("layout/user/sign-up"));
    }

    @Test
    void createUser() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var uriBuilder = fromUriString(ENDPOINT + "/create");

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", userRequestDto.getEmail())
                                .param("username", userRequestDto.getUsername())
                                .param("password", userRequestDto.getPassword())
                                .param("name", userRequestDto.getName())
                                .param("surname", userRequestDto.getSurname())
                                .param("address", userRequestDto.getAddress())
                                .param("phone", userRequestDto.getPhone())
                                .param("birthday", userRequestDto.getBirthday().toString())
                                .param("driverLicenseNumber", userRequestDto.getDriverLicenseNumber())
                                .param("driverLicenseIssueDate", userRequestDto.getDriverLicenseNumber())
                                .param("driverLicenseExpirationDate", userRequestDto.getDriverLicenseNumber())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/sign-up"));
    }

    @Test
    void update() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        assertExpectedIsSaved(savedUser, savedUser.getId());

        var userUpdateRequestDto = TestDtoBuilder.updateUserRequestDto();

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", userUpdateRequestDto.getEmail())
                                .param("username", userUpdateRequestDto.getUsername())
                                .param("role", userUpdateRequestDto.getRole().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + savedUser.getId()));
    }

    @Test
    void delete() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        assertExpectedIsSaved(savedUser, savedUser.getId());

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId() + "/delete");

        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> userService.getById(savedUser.getId()));

        assertEquals("404 NOT_FOUND \"User with id 3 does not exist.\"", result.getMessage());
    }

    @Test
    void changePasswordForm() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/change-password");

        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("change_password"))
                .andExpect(view().name("layout/user/change-password"));
    }

    @Test
    void changePassword() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        assertExpectedIsSaved(savedUser, savedUser.getId());

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId() + "/change-password");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("oldPassword", "Testtest!123")
                                .param("newPassword", "testTest1!123")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + savedUser.getId()));
    }

    @Test
    void changeRole() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        assertExpectedIsSaved(savedUser, savedUser.getId());

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId() + "/change-role");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("role", Role.CLIENT.toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));
    }

    @Test
    @WithMockUser(username = "test", authorities = {"TEST"})
    void return403WithWrongAuthorityUpdate() throws Exception {
        var userCreateRequestDTO = TestDtoBuilder.createUserRequestDto();

        var saved = userService.create(userCreateRequestDTO);
        var expected = saved.get();

        var userUpdateRequestDTO = TestDtoBuilder.updateUserRequestDto();
        UriComponentsBuilder uriBuilder = fromUriString(ENDPOINT + "/" + expected.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("email", userUpdateRequestDTO.getEmail())
                                .param("username", userUpdateRequestDTO.getUsername())
                                .param("role", userUpdateRequestDTO.getRole().toString()))
                .andExpect(status().isForbidden());
    }

    private void assertExpectedIsSaved(UserResponseDto expectedUser, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("user"))
                .andReturn();

        var user = (UserResponseDto) result.getModelAndView().getModel().get("user");

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(user.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(user.getRole()).isEqualTo(expectedUser.getRole());

        assertThat(user.getUserDetailsDto().getId()).isEqualTo(expectedUser.getUserDetailsDto().getId());
        assertThat(user.getUserDetailsDto().getName()).isEqualTo(expectedUser.getUserDetailsDto().getName());
        assertThat(user.getUserDetailsDto().getSurname()).isEqualTo(expectedUser.getUserDetailsDto().getSurname());
        assertThat(user.getUserDetailsDto().getAddress()).isEqualTo(expectedUser.getUserDetailsDto().getAddress());
        assertThat(user.getUserDetailsDto().getPhone()).isEqualTo(expectedUser.getUserDetailsDto().getPhone());
        assertThat(user.getUserDetailsDto().getBirthday()).isEqualTo(expectedUser.getUserDetailsDto().getBirthday());

        assertThat(user.getDriverLicenseDto().getId()).isEqualTo(expectedUser.getDriverLicenseDto().getId());
        assertThat(user.getDriverLicenseDto().getDriverLicenseNumber()).isEqualTo(expectedUser.getDriverLicenseDto().getDriverLicenseNumber());
        assertThat(user.getDriverLicenseDto().getDriverLicenseIssueDate()).isEqualTo(expectedUser.getDriverLicenseDto().getDriverLicenseIssueDate());
        assertThat(user.getDriverLicenseDto().getDriverLicenseExpirationDate()).isEqualTo(expectedUser.getDriverLicenseDto().getDriverLicenseExpirationDate());
    }
}