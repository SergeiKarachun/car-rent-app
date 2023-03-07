package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.DriverLicense;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.domain.projection.DriverLicenseFullView;
import by.sergey.carrentapp.integration.annatation.IT;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.DriverLicenseRepository;
import by.sergey.carrentapp.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class DriverLicenseRepositoryTestIT {

    private final DriverLicenseRepository driverLicenseRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Test
    void saveDriverLicense() {
        DriverLicense driverLicenseToSave = TestEntityBuilder.createDriverLicense();
        UserDetails userDetails = userDetailsRepository.findById(EXISTS_USER_DETAILS_ID).get();

        driverLicenseToSave.setUserDetails(userDetails);
        DriverLicense savedDriverLicense = driverLicenseRepository.saveAndFlush(driverLicenseToSave);

        assertThat(savedDriverLicense.getId()).isNotNull();
        assertThat(savedDriverLicense).isEqualTo(driverLicenseToSave);

    }

    @Test
    void updateDriverLicense() {
        DriverLicense driverLicenseToUpdate = driverLicenseRepository.findById(EXISTS_DRIVER_LICENSE_ID).get();
        driverLicenseToUpdate.setExpirationDate(LocalDate.of(2030,4,1));
        driverLicenseToUpdate.setNumber("DL123456");
        driverLicenseRepository.saveAndFlush(driverLicenseToUpdate);

        DriverLicense updatedeDriverLicense = driverLicenseRepository.findById(driverLicenseToUpdate.getId()).get();

        assertThat(updatedeDriverLicense).isEqualTo(driverLicenseToUpdate);
    }

    @Test
    void deleteDriverLicense() {
        Optional<DriverLicense> toDelete = driverLicenseRepository.findById(DRIVEL_LICENSE_ID_FOR_DELETE);

        toDelete.ifPresent(dl -> driverLicenseRepository.delete(dl));

        Optional<DriverLicense> deletedDL = driverLicenseRepository.findById(DRIVEL_LICENSE_ID_FOR_DELETE);
        assertThat(deletedDL).isEmpty();
    }

    @Test
    void findByNumberIgnoreCase() {
        Optional<DriverLicense> actual = driverLicenseRepository.findByNumberIgnoreCase("aB12345");
        actual.ifPresent(driverLicense -> assertEquals(ExistsEntityBuilder.getExistDriverLicense(), driverLicense));
    }

    @Test
    void findByUserId() {
        Optional<DriverLicense> actual = driverLicenseRepository.findById(EXISTS_DRIVER_LICENSE_ID);

        actual.ifPresent(driverLicense -> assertEquals(ExistsEntityBuilder.getExistDriverLicense(), driverLicense));
    }

    @Test
    void findByExpirationDateLessThanEqual() {
        List<DriverLicense> actualDriverLicenses = driverLicenseRepository
                .findByExpirationDateLessThanEqual(LocalDate.of(2024, 12, 1));

        assertThat(actualDriverLicenses).hasSize(1)
                .containsExactlyInAnyOrder(driverLicenseRepository.findById(DRIVEL_LICENSE_ID_FOR_DELETE).get());
    }

    @Test
    void findDriverLicenseFullViewExpirationDateLess() {
        List<DriverLicenseFullView> driverLicensesFullView = driverLicenseRepository
                .findDriverLicenseFullViewExpirationDateLess(LocalDate.of(2025, 4, 1));

        assertThat(driverLicensesFullView).hasSize(2);

        List<String> numbers = driverLicensesFullView
                .stream()
                .map(dr -> dr.getNumber()).collect(Collectors.toList());
        List<String> names = driverLicensesFullView
                .stream()
                .map(dr -> dr.getFirstname()).collect(Collectors.toList());

       assertThat(numbers).containsExactlyInAnyOrder("AB12345", "AB23456");
       assertThat(names).containsExactlyInAnyOrder("Sergey", "Petr");
    }
}