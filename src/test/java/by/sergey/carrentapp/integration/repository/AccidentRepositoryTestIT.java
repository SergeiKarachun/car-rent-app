package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.Accident;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.domain.projection.AccidentFullView;
import by.sergey.carrentapp.integration.annatation.IT;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.AccidentRepository;
import by.sergey.carrentapp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class AccidentRepositoryTestIT {

    private final AccidentRepository accidentRepository;
    private final OrderRepository orderRepository;

    @Test
    void saveAccident() {
        Accident accidentToSave = TestEntityBuilder.createAccident();
        Order existOrder = orderRepository.findById(EXISTS_ORDER_ID).get();
        existOrder.setAccident(accidentToSave);

        Accident savedAccident = accidentRepository.saveAndFlush(accidentToSave);

        assertNotNull(savedAccident.getId());
    }

    @Test
    void updateAccident() {
        Accident accidentToUpdate = accidentRepository.findById(EXISTS_ACCIDENT_ID).get();
        Order existOrder = orderRepository.findById(EXISTS_ORDER_ID).get();
        accidentToUpdate.setDescription("test description");
        accidentToUpdate.setOrder(existOrder);

        Accident updatedAccident = accidentRepository.saveAndFlush(accidentToUpdate);

        assertThat(updatedAccident).isEqualTo(accidentToUpdate);
        assertThat(updatedAccident.getOrder().getId()).isEqualTo(existOrder.getId());

        Order order = orderRepository.findById(EXISTS_ORDER_ID).get();
        assertThat(order.getAccidents()).contains(updatedAccident);
    }

    @Test
    void deleteAccident() {
        Optional<Accident> accidentToDelete = accidentRepository.findById(ACCIDENT_ID_FOR_DELETE);
        accidentToDelete.ifPresent(ac -> accidentRepository.delete(ac));

        Optional<Accident> deletedAccident = accidentRepository.findById(ACCIDENT_ID_FOR_DELETE);
        assertThat(deletedAccident).isEmpty();
    }

    @Test
    void findAllByOrderId() {
        List<Accident> actualAccident = accidentRepository.findAllByOrderId(EXISTS_ACCIDENT_ID);
        assertThat(actualAccident).hasSize(1).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistAccident());
    }

    @Test
    void findAllByAccidentDateAfter() {
        List<Accident> actualAccidents = accidentRepository.findAllByAccidentDateAfter(LocalDateTime.of(2022, 10, 1, 12, 35, 0));
        assertThat(actualAccidents).hasSize(1);
    }

    @Test
    void findAllByDamage() {
        List<Accident> actualAccidents = accidentRepository.findAllByDamage(BigDecimal.valueOf(30).setScale(2));
        assertThat(actualAccidents).hasSize(1).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistAccident());
    }

    @Test
    void findAllByNameAndSurname() {
        List<Accident> actualAccidents = accidentRepository.findAllByNameAndSurname("SeRgeY", "IvaNoN");
        assertThat(actualAccidents).hasSize(2).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistAccident(),
                accidentRepository.findById(ACCIDENT_ID_FOR_DELETE).get());
    }

    @Test
    void findByCarNumber() {
        List<Accident> actualAccidents = accidentRepository.findByCarNumber("7594");

        assertThat(actualAccidents).hasSize(1).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistAccident());
    }

    @Test
    void updateDamage() {
        accidentRepository.updateDamage(BigDecimal.valueOf(60).setScale(2), 1L);
        Optional<Accident> actual = accidentRepository.findById(EXISTS_ACCIDENT_ID);
        actual.ifPresent(accident -> assertEquals(BigDecimal.valueOf(60).setScale(2), accident.getDamage()));
    }

    @Test
    void findByIdFull() {
        AccidentFullView actual = accidentRepository.findByIdFull(EXISTS_ACCIDENT_ID).get();

        assertThat(actual.getFullName()).isEqualTo("Sergey Ivanon");
        assertThat(actual.getCarNumber()).isEqualTo("7594AB-7");
        assertThat(actual.getBrandName()).isEqualTo("Audi");
        assertThat(actual.getDamage()).isEqualByComparingTo("50.00");
        assertThat(actual.getOrderId()).isEqualTo(1L);
    }

    @Test
    void findAllFull() {
        List<AccidentFullView> actual = accidentRepository.findAllFull();

        assertThat(actual).hasSize(2);

        List<String> listOfModelName = actual.stream()
                .map(accidentFullView -> accidentFullView.getModelName())
                .collect(Collectors.toList());

        List<String> listOfBrandName = actual.stream()
                .map(accidentFullView -> accidentFullView.getBrandName())
                .collect(Collectors.toList());

        assertThat(listOfModelName).containsExactlyInAnyOrder("Rapid", "A6");
        assertThat(listOfBrandName).containsExactlyInAnyOrder("Skoda", "Audi");
    }
}