package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.*;
import by.sergey.carrentapp.domain.model.OrderStatus;
import by.sergey.carrentapp.domain.projection.OrderFullView;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.CarRepository;
import by.sergey.carrentapp.repository.OrderRepository;
import by.sergey.carrentapp.repository.RentalTimeRepository;
import by.sergey.carrentapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static org.assertj.core.api.Assertions.*;


@RequiredArgsConstructor
class OrderRepositoryTestIT extends IntegrationTestBase {

    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalTimeRepository rentalTimeRepository;

    @Test
    void saveOrder() {
        Order orderToSave = TestEntityBuilder.createOrder();
        Car car = carRepository.findById(EXISTS_CAR_ID).get();
        User user = userRepository.findById(EXISTS_USER_ID).get();
        orderToSave.setUser(user);
        orderToSave.setCar(car);

        RentalTime rentalTime = TestEntityBuilder.createRentalTime();
        rentalTime.setOrder(orderToSave);

        Order savedOrder = orderRepository.saveAndFlush(orderToSave);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void shouldCreateOrderWithNotExistsAccidents() {
        User user = userRepository.findById(EXISTS_USER_ID).get();
        Car car = carRepository.findById(EXISTS_CAR_ID).get();
        Accident accidentToSave = TestEntityBuilder.createAccident();
        Order orderToSave = TestEntityBuilder.createOrder();
        orderToSave.setUser(user);
        orderToSave.setCar(car);
        orderToSave.setAccident(accidentToSave);
        RentalTime rentalTime = TestEntityBuilder.createRentalTime();
        rentalTime.setOrder(orderToSave);

        orderRepository.saveAndFlush(orderToSave);

        assertThat(orderToSave.getId()).isNotNull();
        assertThat(accidentToSave.getId()).isNotNull();
        assertThat(orderToSave.getAccidents()).contains(accidentToSave);
        assertThat(accidentToSave.getOrder().getId()).isEqualTo(orderToSave.getId());
    }

    @Test
    void updateOrder() {
        LocalDateTime startRentalDate = LocalDateTime.of(2022, 10, 11, 13, 0);
        Order orderToUpdate = orderRepository.findById(EXISTS_ORDER_ID).get();
        RentalTime rentalTime = orderToUpdate.getRentalTime();
        rentalTime.setStartRentalDate(startRentalDate);
        rentalTime.setOrder(orderToUpdate);

        orderRepository.saveAndFlush(orderToUpdate);

        Order updatedOrder = orderRepository.findById(orderToUpdate.getId()).get();

        RentalTime actualRentalTime = rentalTimeRepository.findById(EXISTS_RENTAL_TIME_ID).get();

        assertThat(updatedOrder).isEqualTo(orderToUpdate);
        assertThat(updatedOrder.getRentalTime().getStartRentalDate()).isEqualTo(startRentalDate);
        assertThat(actualRentalTime.getStartRentalDate()).isEqualTo(startRentalDate);
    }

    @Test
    void deleteOrder() {
        Optional<Order> orderToDelete = orderRepository.findById(ORDER_ID_FOR_DELETE);

        orderToDelete.ifPresent(or -> orderRepository.delete(or));

        assertThat(orderRepository.findById(ORDER_ID_FOR_DELETE)).isEmpty();
    }


    @Test
    void findAllByDateBetween() {

        LocalDate start = LocalDate.of(2022, 7, 10);
        LocalDate end = LocalDate.of(2022, 7, 11);

        List<Order> actualOrder = orderRepository.findAllByDateBetween(start, end);

        List<LocalDate> dates = actualOrder.stream().map(Order::getDate).toList();

        dates.forEach(data -> assertThat(data).isAfterOrEqualTo(start));
        dates.forEach(data -> assertThat(data).isBeforeOrEqualTo(end));

    }

    @Test
    void findAllByCarNumber() {
        List<Order> allByCarNumber = orderRepository.findAllByCarNumber(ExistsEntityBuilder.getExistCar().getCarNumber());

        assertThat(allByCarNumber).hasSize(1).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistOrder());
    }

    @Test
    void findAllByCarId() {
        List<Order> actualOrders = orderRepository.findAllByCarId(EXISTS_CAR_ID);

        assertThat(actualOrders).hasSize(1).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistOrder());
    }

    @Test
    void findAllByUserId() {
        List<Order> actualOrders = orderRepository.findAllByUserId(EXISTS_USER_ID);
        List<Long> idCars = actualOrders.stream().map(order -> order.getId()).collect(Collectors.toList());

        assertThat(idCars).hasSize(2).containsExactlyInAnyOrder(1L, 3L);
    }

    @Test
    void findAllByDate() {
        List<Order> actualOrders = orderRepository.findAllByDate(ExistsEntityBuilder.getExistOrder().getDate());

        assertThat(actualOrders).hasSize(1).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistOrder());
    }

    @Test
    void findAllByOrderStatus() {
        List<Order> actualOrders = orderRepository.findAllByOrderStatus(OrderStatus.APPROVED);

        assertThat(actualOrders).hasSize(1).containsExactlyInAnyOrder(ExistsEntityBuilder.getExistOrder());
    }

    @Test
    void findOrderWithAccident() {
        List<Order> actualOrders = orderRepository.findOrderWithAccident();
        Order existOrder = orderRepository.findById(EXISTS_ORDER_ID).get();

        List<Order> expectOrders = orderRepository.findAllById(List.of(1L, 3L));

        assertThat(actualOrders).hasSize(2).isEqualTo(expectOrders);
    }

    @Test
    void findAllFullView() {
        List<OrderFullView> ordersFullView = orderRepository.findAllFullView();

        assertThat(ordersFullView).hasSize(3);

        List<OrderStatus> orderStatuses = ordersFullView.stream().map(OrderFullView::getOrderStatus).collect(Collectors.toList());

        assertThat(orderStatuses).containsExactlyInAnyOrder(OrderStatus.CONFIRMATION, OrderStatus.COMPLETED, OrderStatus.APPROVED);
    }

}