package by.sergey.carrentapp.integration.service;

import by.sergey.carrentapp.domain.dto.rentaltime.RentalTimeResponseDto;
import by.sergey.carrentapp.domain.dto.rentaltime.RentalTimeUpdateRequestDto;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.service.RentalTimeService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;


@RequiredArgsConstructor
class RentalTimeServiceTestIT extends IntegrationTestBase {

    private final RentalTimeService rentalTimeService;

    @Test
    void getById() {
        var actualRentalTime = rentalTimeService.getById(EXISTS_RENTAL_TIME_ID);

        assertThat(actualRentalTime.get()).isNotNull();
    }

    @Test
    void update() {
        var rentalTime = rentalTimeService.getById(EXISTS_RENTAL_TIME_ID);
        var rentalTimeUpdateRequestDto = new RentalTimeUpdateRequestDto(LocalDateTime.now(), LocalDateTime.now().plusDays(2));

        var actual = rentalTimeService.update(rentalTime.get().getId(), rentalTimeUpdateRequestDto);

        assertThat(actual).isNotNull();
        actual.ifPresent(ac -> {
            assertEquals(rentalTimeUpdateRequestDto.getStartRentalTime(), ac.getStartRentalTime());
            assertEquals(rentalTimeUpdateRequestDto.getEndRentalTime(), ac.getEndRentalTime());
        });
    }

    @Test
    void deleteById() {
        assertTrue(rentalTimeService.deleteById(RENTAL_TIME_ID_FOR_DELETE));
    }

    @Test
    void getAll() {
        var allRentalTimes = rentalTimeService.getAll();

        assertThat(allRentalTimes).hasSize(3);

        var startRentalTimes = allRentalTimes.stream().map(RentalTimeResponseDto::getStartRentalTime).collect(toList());
        assertThat(startRentalTimes).containsExactlyInAnyOrder(LocalDateTime.of(2022, 7, 11, 0, 0, 0),
                LocalDateTime.of(2022, 10, 1, 0, 0, 0),
                LocalDateTime.of(2022, 12, 12, 0, 0, 0));
    }
}