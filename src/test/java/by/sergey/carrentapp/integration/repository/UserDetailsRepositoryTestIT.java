package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.integration.annatation.IT;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.UserDetailsRepository;
import by.sergey.carrentapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class UserDetailsRepositoryTestIT {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;

    @Test
    void saveUserDetails() {
        UserDetails userDetailsToSave = TestEntityBuilder.createUserDetails();
        User user = TestEntityBuilder.createUser();
        userDetailsToSave.setUser(user);
        userRepository.saveAndFlush(user);
        UserDetails savedUserDetails = userDetailsRepository.saveAndFlush(userDetailsToSave);

        assertThat(savedUserDetails.getId()).isNotNull();
    }

    @Test
    void updateUserDetails() {
        UserDetails userDetailsToUpdate = userDetailsRepository.findById(EXISTS_USER_DETAILS_ID).get();
        userDetailsToUpdate.setName("Ignat");
        userDetailsRepository.saveAndFlush(userDetailsToUpdate);

        UserDetails updatedUserDetails = userDetailsRepository.findById(EXISTS_USER_DETAILS_ID).get();

        assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
    }

    @Test
    void deleteUserDetails() {
        Optional<UserDetails> toDelete = userDetailsRepository.findById(USER_DETAILS_ID_FOR_DELETE);

        toDelete.ifPresent(ud -> userDetailsRepository.delete(ud));

        Optional<UserDetails> deleted = userDetailsRepository.findById(USER_DETAILS_ID_FOR_DELETE);
        assertThat(deleted).isEmpty();
    }

    @Test
    void findByUserId() {
        Optional<UserDetails> actual = Optional.of(userDetailsRepository.findByUserId(EXISTS_USER_ID));

        actual.ifPresent(ud -> assertEquals(ExistsEntityBuilder.getExistUserDetails(), ud));
    }

    @Test
    void findAllByRegistrationDateBetween() {
        List<UserDetails> actualUsers = userDetailsRepository.findAllByRegistrationDateBetween(LocalDate.of(2022, 9, 22),
                LocalDate.of(2022, 12, 12));

        assertThat(actualUsers).hasSize(1);
        assertThat(actualUsers.get(0).getSurname()).isEqualTo(ExistsEntityBuilder.getExistUserDetails().getSurname());
    }

    @Test
    void findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase() {
        List<UserDetails> actual = userDetailsRepository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase("serg", "van");

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(ExistsEntityBuilder.getExistUserDetails());
    }

    @Test
    void findByPhone() {
        Optional<UserDetails> actual = userDetailsRepository.findByPhone("+37533 1234567");

        actual.ifPresent(act -> assertEquals(ExistsEntityBuilder.getExistUserDetails(), act));
    }
}