package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.domain.entity.RentalTime;
import by.sergey.carrentapp.integration.annatation.IT;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.OrderRepository;
import by.sergey.carrentapp.repository.RentalTimeRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static org.assertj.core.api.Assertions.*;

@IT
@RequiredArgsConstructor
class RentalTimeRepositoryTestIT {

    private final RentalTimeRepository rentalTimeRepository;
    private final OrderRepository orderRepository;

    @Test
    void saveRentalTime() {
        RentalTime rentalTimeToSave = TestEntityBuilder.createRentalTime();
        Order order = orderRepository.findById(4L).get();
        rentalTimeToSave.setOrder(order);

        RentalTime savedRentalTime = rentalTimeRepository.saveAndFlush(rentalTimeToSave);

        assertThat(savedRentalTime.getId()).isNotNull();
        assertThat(savedRentalTime.getOrder()).isEqualTo(order);
        assertThat(order.getRentalTime()).isEqualTo(savedRentalTime);
    }

    @Test
    void updateRentalTime() {
        RentalTime rentalTimeToUpdate = rentalTimeRepository.findById(EXISTS_RENTAL_TIME_ID).get();

        rentalTimeToUpdate.setStartRentalDate(LocalDateTime.of(2023,1,1, 10,30));
        rentalTimeToUpdate.setEndRentalDate(LocalDateTime.of(2023,1,3, 8,30));

        RentalTime updatedRentalTime = rentalTimeRepository.saveAndFlush(rentalTimeToUpdate);

        assertThat(updatedRentalTime.getStartRentalDate()).isEqualTo(LocalDateTime.of(2023,1,1, 10,30));
        assertThat(updatedRentalTime.getEndRentalDate()).isEqualTo(LocalDateTime.of(2023,1,3, 8,30));
    }

    @Test
    void deleteRentalTime() {
        Optional<RentalTime> rentalTimeToDelete = rentalTimeRepository.findById(RENTAL_TIME_ID_FOR_DELETE);

        rentalTimeToDelete.ifPresent(rt -> rentalTimeRepository.delete(rt));

        assertThat(rentalTimeRepository.findById(RENTAL_TIME_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void findByOrderId() {
        Optional<RentalTime> actualRentalTime = rentalTimeRepository.findByOrderId(EXISTS_ORDER_ID);
        actualRentalTime.ifPresent(time -> assertThat(time).isEqualTo(ExistsEntityBuilder.getExistRentalTime()) );
    }

    @Test
    void findAllRentalTime() {
        List<RentalTime> all = rentalTimeRepository.findAll();
        assertThat(all).hasSize(3).contains(ExistsEntityBuilder.getExistRentalTime());

        List<LocalDateTime> listOfStartRentalTime = all.stream().map(time -> time.getStartRentalDate()).collect(Collectors.toList());

        assertThat(listOfStartRentalTime).containsExactlyInAnyOrder(
                LocalDateTime.of(2022,7,11,0,0),
                LocalDateTime.of(2022,10,1,0,0),
                LocalDateTime.of(2022,12,12,0,0));

    }
}