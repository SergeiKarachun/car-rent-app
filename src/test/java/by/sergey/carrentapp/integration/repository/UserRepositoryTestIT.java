package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.domain.model.Role;
import by.sergey.carrentapp.integration.annatation.IT;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class UserRepositoryTestIT {

    private final UserRepository userRepository;

    @Test
    void saveUser() {
        User userToSave = TestEntityBuilder.createUser();
        User savedUser = userRepository.saveAndFlush(userToSave);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser).isEqualTo(userToSave);
    }

    @Test
    void updateUser() {
        User userToUpdate = userRepository.findById(EXISTS_USER_ID).get();
        userToUpdate.setName("vasek");
        userToUpdate.setEmail("vasel@mail.com");
        userToUpdate.setRole(Role.CLIENT);

        User updatedUser = userRepository.saveAndFlush(userToUpdate);

        assertThat(updatedUser.getName()).isEqualTo("vasek");
        assertThat(updatedUser.getEmail()).isEqualTo("vasel@mail.com");
        assertThat(updatedUser.getRole()).isEqualTo(Role.CLIENT);
        assertThat(updatedUser).isEqualTo(userToUpdate);
    }

    @Test
    void deleteUser() {
        Optional<User> userToDelete = userRepository.findById(USER_ID_FOR_DELETE);

        userToDelete.ifPresent(user -> userRepository.delete(user));

        assertThat(userRepository.findById(USER_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void findByEmailAndPassword() {
        Optional<User> actualUser = userRepository
                .findByEmailAndPassword(ExistsEntityBuilder.getExistUser().getEmail(),
                                        ExistsEntityBuilder.getExistUser().getPassword());

        actualUser.ifPresent(user -> assertThat(user).isEqualTo(ExistsEntityBuilder.getExistUser()));
    }

    @Test
    void existsByEmailAndPassword() {
        assertThat(userRepository.existsByEmailAndPassword(ExistsEntityBuilder.getExistUser().getEmail(),
                ExistsEntityBuilder.getExistUser().getPassword())).isTrue();
    }

    @Test
    void findByEmail() {
        Optional<User> actualUser = userRepository.findByEmail("admin@gmail.com");

        actualUser.ifPresent(user -> assertThat(user).isEqualTo(ExistsEntityBuilder.getExistUser()));
    }

    @Test
    void existsByEmail() {
        assertThat(userRepository.existsByEmail(ExistsEntityBuilder.getExistUser().getEmail())).isTrue();
    }

    @Test
    void findByPhone() {
        Optional<User> actualUser = userRepository.findByPhone("+37533 1234567");

        actualUser.ifPresent(user -> assertThat(user).isEqualTo(ExistsEntityBuilder.getExistUser()));
    }

    @Test
    void findAllWithOrders() {
        List<User> actualUsers = userRepository.findAllWithOrders();
        assertThat(actualUsers).hasSize(2)
                .containsExactlyInAnyOrderElementsOf(userRepository.findAllById(List.of(1L, 2L)));
    }

    @Test
    void findAllWithoutOrders() {
        List<User> actualUsers = userRepository.findAllWithoutOrders();
        assertThat(actualUsers).isEmpty();
    }

    @Test
    void findAllWithAccident() {
        List<User> actualUsers = userRepository.findAllWithAccident();
        assertThat(actualUsers).hasSize(1).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistUser());
    }

    @Test
    void findAllWithExpiredDriverLicense() {

    }
}