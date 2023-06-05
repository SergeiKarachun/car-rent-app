package by.sergey.carrentapp.integration.http.rest;

import by.sergey.carrentapp.domain.dto.PageResponse;
import by.sergey.carrentapp.domain.dto.user.UserChangePasswordRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserResponseDto;
import by.sergey.carrentapp.domain.dto.user.UserUpdateRequestDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.repository.DriverLicenseRepository;
import by.sergey.carrentapp.repository.UserDetailsRepository;
import by.sergey.carrentapp.repository.UserRepository;
import by.sergey.carrentapp.service.DriverLicenseService;
import by.sergey.carrentapp.service.UserDetailsService;
import by.sergey.carrentapp.service.UserService;
import by.sergey.carrentapp.utils.predicate.UserPredicateBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserRestControllerTestIT extends IntegrationTestBase {

    static final String ENDPOINT = "/api/v1/users";
    static final String MOCK_USERNAME = "admin@gmail.com";
    private final MockMvc mockMvc;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final DriverLicenseRepository driverLicenseRepository;
    private final UserPredicateBuilder userPredicateBuilder;
    private final ObjectMapper objectMapper;

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
    @Test
    void getAllUsers() throws Exception {
        var result = mockMvc.perform(
                        get(fromUriString(ENDPOINT).build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        var pageResponse = objectMapper.readValue(result.getResponse().getContentAsString(), PageResponse.class);
        assertThat(pageResponse.getContent()).hasSize(2);
    }

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
    @Test
    void createUser() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var uriBuilder = fromUriString(ENDPOINT);
        var requestBody = objectMapper.writeValueAsString(userRequestDto);

        var result = mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        var userResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);

        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseDto.getId()).isNotNull();
        assertThat(userResponseDto.getUsername()).isEqualTo(userRequestDto.getUsername());
        assertThat(userResponseDto.getEmail()).isEqualTo(userRequestDto.getEmail());
        assertThat(userResponseDto.getUserDetailsDto().getAddress()).isEqualTo(userRequestDto.getAddress());
    }

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
    @Test
    void getById() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId());
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        UserResponseDto userResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);
        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseDto.getId()).isEqualTo(savedUser.getId());
        assertThat(userResponseDto.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(userResponseDto.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(userResponseDto.getRole()).isEqualTo(savedUser.getRole());
        assertThat(userResponseDto.getUserDetailsDto()).isEqualTo(savedUser.getUserDetailsDto());
        assertThat(userResponseDto.getDriverLicenseDto()).isEqualTo(savedUser.getDriverLicenseDto());
    }

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
    @Test
    void userNotFound() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/246");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void return401IfUserIsNotAuthorized() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId());
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection());
    }

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
    @Test
    void update() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        var userUpdateRequestDto = TestDtoBuilder.updateUserRequestDto();
        var requestBody = objectMapper.writeValueAsString(userUpdateRequestDto);

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId());
        var result = mockMvc.perform(
                        put(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        UserResponseDto userResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);
        assertThat(userResponseDto.getId()).isEqualTo(savedUser.getId());
        assertThat(userResponseDto.getUsername()).isEqualTo(userUpdateRequestDto.getUsername());
        assertThat(userResponseDto.getEmail()).isEqualTo(userUpdateRequestDto.getEmail());
        assertThat(userResponseDto.getRole()).isEqualTo(userUpdateRequestDto.getRole());
    }

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
    @Test
    void deleteUser() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId());
        mockMvc.perform(
                        delete(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
        assertThat(userRepository.existsById(savedUser.getId())).isFalse();
        assertThat(userDetailsRepository.existsById(savedUser.getId())).isFalse();
        assertThat(driverLicenseRepository.existsById(savedUser.getId())).isFalse();
    }

    @WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
    @Test
    void changePassword() throws Exception {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto).get();
        UserChangePasswordRequestDto userChangePasswordRequestDto = new UserChangePasswordRequestDto(userRequestDto.getPassword(),
                "testTest1!!!!");

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedUser.getId() + "/change-password");
        var result = mockMvc.perform(
                        put(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userChangePasswordRequestDto))
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        UserResponseDto userResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);
        assertThat(userResponseDto.getId()).isEqualTo(savedUser.getId());
    }
}