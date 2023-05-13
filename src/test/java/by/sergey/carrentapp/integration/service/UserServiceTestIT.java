package by.sergey.carrentapp.integration.service;

import by.sergey.carrentapp.domain.dto.filterdto.UserFilter;
import by.sergey.carrentapp.domain.dto.user.UserResponseDto;
import by.sergey.carrentapp.domain.model.Role;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.TestEntityIdConst;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.UserService;
import by.sergey.carrentapp.service.exception.UserBadRequestException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@RequiredArgsConstructor
class UserServiceTestIT extends IntegrationTestBase {

    private final UserService userService;

    @Test
    void create() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var actualUser = userService.create(userRequestDto);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.get().getId()).isNotNull();
        assertSame(Role.CLIENT, actualUser.get().getRole());
    }

    @Test
    void login() {
        var expectedUser = userService.getById(TestEntityIdConst.EXISTS_USER_ID);
        var loginRequestDto = TestDtoBuilder.createLoginRequestDto();

        var actualUser = userService.login(loginRequestDto);

        assertThat(actualUser).isNotNull();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void update() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userRequestDto);
        var userUpdateRequestDto = TestDtoBuilder.updateUserRequestDto();

        var actualUser = userService.update(savedUser.get().getId(), userUpdateRequestDto);

        assertThat(actualUser).isNotNull();
        assertEquals(userUpdateRequestDto.getUsername(), actualUser.get().getUsername());
        assertEquals(userUpdateRequestDto.getRole(), actualUser.get().getRole());
        assertEquals(savedUser.get().getId(), actualUser.get().getId());
    }

    @Test
    void getById() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var expectedUser = userService.create(userRequestDto);

        var actualUser = userService.getById(expectedUser.get().getId());

        assertThat(actualUser).isNotNull();
        actualUser.ifPresent(actual -> assertEquals(expectedUser, actualUser));
    }

    @Test
    void getAll() {
        var listOfEmails = List.of("client@gmail.com", "admin@gmail.com");

        Page<UserResponseDto> users = userService.getAll(UserFilter.builder().build(), 0, 10);

        assertThat(users.getContent()).hasSize(2);
        var actualEmails = users.stream().map(UserResponseDto::getEmail).collect(toList());
        assertTrue(actualEmails.containsAll(listOfEmails));
    }

    @Test
    void getAllWithoutPage() {
        var listOfEmails = List.of("client@gmail.com", "admin@gmail.com");

        var actualUsers = userService.getAllWithoutPage();

        assertThat(actualUsers).hasSize(2);
        var actualEmails = actualUsers.stream().map(UserResponseDto::getEmail).collect(toList());
        assertTrue(actualEmails.containsAll(listOfEmails));
    }

    @Test
    void deleteById() {
        assertTrue(userService.deleteById(TestEntityIdConst.USER_ID_FOR_DELETE));
    }

    @Test
    void checkUsernameIsNotUnique() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var actualUser = userService.create(userRequestDto);
        var username = userRequestDto.getUsername();

        var result = assertThrowsExactly(UserBadRequestException.class, () -> userService.checkUsernameIsUnique(username));

        assertEquals("400 BAD_REQUEST \"User with this username test already exist.\"", result.getMessage());
    }

    @Test
    void checkEmailIsNotUnique() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var actualUser = userService.create(userRequestDto);
        var email = userRequestDto.getEmail();

        var result = assertThrowsExactly(UserBadRequestException.class, () -> userService.checkEmailIsUnique(email));

        assertEquals("400 BAD_REQUEST \"User with this email test@gmail.com already exist.\"", result.getMessage());
    }
}