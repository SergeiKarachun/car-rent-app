package by.sergey.carrentapp.integration.service;

import by.sergey.carrentapp.domain.dto.filterdto.UserDetailsFilter;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsResponseDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.UserDetailsService;
import by.sergey.carrentapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@RequiredArgsConstructor
class UserDetailsServiceTestIT extends IntegrationTestBase {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Test
    void create() {
        var userCreateRequestDTO = TestDtoBuilder.createUserRequestDto();

        var actualUser = userService.create(userCreateRequestDTO);

        assertTrue(actualUser.isPresent());
        assertEquals(userCreateRequestDTO.getName(), actualUser.get().getUserDetailsDto().getName());
        assertEquals(userCreateRequestDTO.getSurname(), actualUser.get().getUserDetailsDto().getSurname());
        assertEquals(userCreateRequestDTO.getAddress(), actualUser.get().getUserDetailsDto().getAddress());
        assertEquals(userCreateRequestDTO.getPhone(), actualUser.get().getUserDetailsDto().getPhone());
    }

    @Test
    void update() {
        var userCreateRequestDTO = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userCreateRequestDTO);
        var userDetailsToUpdate = TestDtoBuilder.createUserDetailsUpdateRequestDto();

        var actualUserDetails = userDetailsService.update(savedUser.get().getId(), userDetailsToUpdate);

        assertThat(actualUserDetails).isNotNull();
        assertEquals(userDetailsToUpdate.getName(), actualUserDetails.get().getName());
        assertEquals(userDetailsToUpdate.getSurname(), actualUserDetails.get().getSurname());
        assertEquals(userDetailsToUpdate.getPhone(), actualUserDetails.get().getPhone());
        assertEquals(userDetailsToUpdate.getAddress(), actualUserDetails.get().getAddress());
    }

    @Test
    void deleteById() {
        assertTrue(userDetailsService.deleteById(USER_DETAILS_ID_FOR_DELETE));
    }

    @Test
    void getById() {
        var userCreateRequestDTO = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userCreateRequestDTO);

        var actualUserDetails = userDetailsService.getById(savedUser.get().getUserDetailsDto().getId());

        assertThat(actualUserDetails).isNotNull();
        actualUserDetails.ifPresent(actual -> assertEquals(savedUser.get().getUserDetailsDto(), actual));
    }

    @Test
    void getByUserId() {
        var userCreateRequestDTO = TestDtoBuilder.createUserRequestDto();
        var savedUser = userService.create(userCreateRequestDTO);

        var actualUserDetails = userDetailsService.getByUserId(savedUser.get().getId());

        assertThat(actualUserDetails).isNotNull();
        actualUserDetails.ifPresent(actual -> assertEquals(savedUser.get().getUserDetailsDto(), actual));
    }

    @Test
    void getAll() {
        var userDetailOnPage = userDetailsService.getAll(UserDetailsFilter.builder().build(), 0, 4);

        assertThat(userDetailOnPage.getContent()).hasSize(2);
        var listOfNames = userDetailOnPage.stream().map(UserDetailsResponseDto::getName).collect(toList());
        assertThat(listOfNames).containsExactlyInAnyOrder("Sergey", "Petr");
    }

    @Test
    void getAllByNameAndSurname() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var userResponseDto = userService.create(userRequestDto);
        var expected = userResponseDto.get().getUserDetailsDto();

        var actualUserDetails = userDetailsService.getAllByNameAndSurname(expected.getName(), expected.getSurname());

        assertThat(actualUserDetails).hasSize(1);
        assertEquals(expected, actualUserDetails.get(0));
    }

}