package by.sergey.carrentapp.integration.service;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseResponseDto;
import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseUpdateRequestDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.DriverLicenseService;
import by.sergey.carrentapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;


@RequiredArgsConstructor
class DriverLicenseServiceTestIT extends IntegrationTestBase {

    private final DriverLicenseService driverLicenseService;
    private final UserService userService;

    @Test
    void create() {
        var expectedUser = TestDtoBuilder.createUserRequestDto();

        var actualUser = userService.create(expectedUser);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.get().getDriverLicenseDto().getId()).isNotNull();
        assertEquals(expectedUser.getDriverLicenseNumber(), actualUser.get().getDriverLicenseDto().getDriverLicenseNumber());
        assertEquals(expectedUser.getDriverLicenseExpirationDate(), actualUser.get().getDriverLicenseDto().getDriverLicenseExpirationDate());
        assertEquals(expectedUser.getDriverLicenseIssueDate(), actualUser.get().getDriverLicenseDto().getDriverLicenseIssueDate());
    }

    @Test
    void update() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedDriverLicenseDto = userService.create(userRequestDto).get().getDriverLicenseDto();
        var licenseToUpdateRequestDto = new DriverLicenseUpdateRequestDto("BC123546",
                LocalDate.now(),
                LocalDate.now().plusYears(3));

        var actualDriverLicense = driverLicenseService.update(savedDriverLicenseDto.getId(), licenseToUpdateRequestDto);

        assertThat(actualDriverLicense).isNotNull();
        actualDriverLicense.ifPresent(actual -> {
            assertEquals(licenseToUpdateRequestDto.getDriverLicenseNumber(), actual.getDriverLicenseNumber());
            assertEquals(licenseToUpdateRequestDto.getDriverLicenseIssueDate(), actual.getDriverLicenseIssueDate());
            assertEquals(licenseToUpdateRequestDto.getDriverLicenseExpirationDate(), actual.getDriverLicenseExpirationDate());
        });
    }

    @Test
    void getById() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedDriverLicenseDto = userService.create(userRequestDto).get().getDriverLicenseDto();

        var actualDriverLicense = driverLicenseService.getById(savedDriverLicenseDto.getId());

        assertThat(actualDriverLicense).isNotNull();
        actualDriverLicense.ifPresent(actual -> assertEquals(savedDriverLicenseDto, actual));
    }

    @Test
    void deleteById() {
        assertTrue(driverLicenseService.deleteById(DRIVEL_LICENSE_ID_FOR_DELETE));
    }

    @Test
    void getAll() {
        var listOfDriverLicenseNumbers = List.of("AB12345", "AB23456");

        Page<DriverLicenseResponseDto> driverLicenseResponseDtoPage = driverLicenseService.getAll(0, 10);

        assertThat(driverLicenseResponseDtoPage.getContent()).hasSize(2);
        var actualDriverLicenseNumbers = driverLicenseResponseDtoPage.stream().map(DriverLicenseResponseDto::getDriverLicenseNumber).collect(toList());
        assertTrue(actualDriverLicenseNumbers.containsAll(listOfDriverLicenseNumbers));
    }

    @Test
    void getByUserId() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedDriverLicenseDto = userService.create(userRequestDto).get().getDriverLicenseDto();

        var actualDriverLicense = driverLicenseService.getByUserId(savedDriverLicenseDto.getUserId());

        assertThat(actualDriverLicense).isNotNull();
        actualDriverLicense.ifPresent(actual -> assertEquals(savedDriverLicenseDto, actual));
    }

    @Test
    void getByNumber() {
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var savedDriverLicenseDto = userService.create(userRequestDto).get().getDriverLicenseDto();

        var actualDriverLicense = driverLicenseService.getByNumber(savedDriverLicenseDto.getDriverLicenseNumber());

        assertThat(actualDriverLicense).isNotNull();
        actualDriverLicense.ifPresent(actual -> assertEquals(savedDriverLicenseDto, actual));
    }

    @Test
    void getAllExpiredDriverLicenses() {
        var allExpiredDriverLicenses = driverLicenseService.getAllExpiredDriverLicenses();

        assertThat(allExpiredDriverLicenses).hasSize(0);
    }
}