package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseResponseDto;
import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.DriverLicenseService;
import by.sergey.carrentapp.service.UserService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static by.sergey.carrentapp.integration.http.controller.DriverLicenseControllerTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
class DriverLicenseControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gmail.com";
    static final String ENDPOINT = "/driver-licenses";
    private final MockMvc mockMvc;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final DriverLicenseService driverLicenseService;
    private final UserService userService;

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
                .andExpect(view().name("layout/user/driver-licenses"))
                .andExpect(model().attributeExists("driverLicensePage"))
                .andReturn();

        var driverLicensePage = ((Page<DriverLicenseResponseDto>) result.getModelAndView().getModel().get("driverLicensePage")).getContent();
        assertThat(driverLicensePage).hasSize(2);
    }

    @Test
    void getById() throws Exception {
        DriverLicenseResponseDto expectedDriverLicense = getDriverLicense();

        assertExpectedIsSaved(expectedDriverLicense, expectedDriverLicense.getId());
    }

    @Test
    void update() throws Exception {
        var driverLicense = getDriverLicense();
        var driverLicenseUpdateRequestDto = TestDtoBuilder.createDriverLicenseUpdateRequestDto();

        var uriBuilder = fromUriString(ENDPOINT + "/" + driverLicense.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("driverLicenseNumber", driverLicenseUpdateRequestDto.getDriverLicenseNumber())
                                .param("driverLicenseIssueDate", driverLicenseUpdateRequestDto.getDriverLicenseIssueDate().toString())
                                .param("driverLicenseExpirationDate", driverLicenseUpdateRequestDto.getDriverLicenseExpirationDate().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/users/profile/{\\d+}"));
    }

    @Test
    void deleteById() throws Exception {
        var driverLicense = getDriverLicense();

        var uriBuilder = fromUriString(ENDPOINT + "/" + driverLicense.getId() + "/delete");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> driverLicenseService.getById(driverLicense.getId()));

        assertEquals("404 NOT_FOUND \"Driver license with id 3 does not exist.\"", result.getMessage());
    }


    @Test
    void getByUserId() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();
        var expectedDriverLicense = savedUser.getDriverLicenseDto();

        var uriBuilder = fromUriString(ENDPOINT + "/by-user-id");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("userId", savedUser.getId().toString())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/user/driver-license"))
                .andExpect(model().attributeExists("driverLicense"))
                .andReturn();

        var driverLicense = (DriverLicenseResponseDto) result.getModelAndView().getModel().get("driverLicense");
        assertThat(driverLicense).isEqualTo(expectedDriverLicense);
    }

    @Test
    void getByNumber() throws Exception {
        var driverLicense = getDriverLicense();

        var uriBuilder = fromUriString(ENDPOINT + "/by-number");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("number", driverLicense.getDriverLicenseNumber())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/user/driver-license"))
                .andExpect(model().attributeExists("driverLicense"))
                .andReturn();

        var driverLicenseDTO = (DriverLicenseResponseDto) result.getModelAndView().getModel().get("driverLicense");

        assertThat(driverLicenseDTO).isEqualTo(driverLicense);
    }

    @Test
    void getAllExpired() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/all-expired");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("driverLicensePage"))
                .andReturn();

        var driverLicensePage = ((Page<DriverLicenseResponseDto>) result.getModelAndView().getModel().get("driverLicensePage")).getContent();
        assertThat(driverLicensePage).isEmpty();
    }

    private DriverLicenseResponseDto getDriverLicense() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();
        var expectedDriverLicense = savedUser.getDriverLicenseDto();
        return expectedDriverLicense;
    }

    private void assertExpectedIsSaved(DriverLicenseResponseDto expectedDriverLicense, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("driverLicense"))
                .andReturn();

        var driverLicense = (DriverLicenseResponseDto) result.getModelAndView().getModel().get("driverLicense");

        assertThat(driverLicense.getId()).isEqualTo(id);
        assertThat(driverLicense.getDriverLicenseNumber()).isEqualTo(expectedDriverLicense.getDriverLicenseNumber());
        assertThat(driverLicense.getDriverLicenseIssueDate()).isEqualTo(expectedDriverLicense.getDriverLicenseIssueDate());
        assertThat(driverLicense.getDriverLicenseExpirationDate()).isEqualTo(expectedDriverLicense.getDriverLicenseExpirationDate());
    }
}