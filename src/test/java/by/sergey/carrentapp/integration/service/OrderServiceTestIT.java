package by.sergey.carrentapp.integration.service;

import by.sergey.carrentapp.domain.dto.order.OrderResponseDto;
import by.sergey.carrentapp.domain.dto.order.OrderUpdateRequestDto;
import by.sergey.carrentapp.domain.model.OrderStatus;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@RequiredArgsConstructor
class OrderServiceTestIT extends IntegrationTestBase {

    private final OrderService orderService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final CarService carService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Test
    void create() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());
        var carResponseDto = carService.create(carRequestDto);
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var userResponseDto = userService.create(userRequestDto);
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(userResponseDto.get().getId(), carResponseDto.get().getId());

        var actualOrder = orderService.create(orderRequestDto);

        assertThat(actualOrder).isNotNull();
        actualOrder.ifPresent(actual -> {
            assertEquals(orderRequestDto.getStartRentalDate(), actual.getStartRentalDate());
            assertEquals(orderRequestDto.getEndRentalDate(), actual.getEndRentalDate());
            assertEquals(orderRequestDto.getUserId(), actual.getUser().getId());
            assertEquals(orderRequestDto.getCarId(), actual.getCar().getId());
        });
        assertThat(actualOrder.get().getId()).isNotNull();
    }

    @Test
    void update() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());
        var carResponseDto = carService.create(carRequestDto);
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var userResponseDto = userService.create(userRequestDto);
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(userResponseDto.get().getId(), carResponseDto.get().getId());
        var savedOrder = orderService.create(orderRequestDto);
        var orderUpdateRequestDto = new OrderUpdateRequestDto(
                carResponseDto.get().getId(),
                LocalDateTime.of(2023, 5, 13, 12, 00),
                LocalDateTime.of(2023, 5, 23, 12, 00));

        var actualOrder = orderService.update(savedOrder.get().getId(), orderUpdateRequestDto);

        assertThat(actualOrder).isNotNull();
        actualOrder.ifPresent(actual -> {
            assertEquals(orderUpdateRequestDto.getStartRentalDate(), actual.getStartRentalDate());
            assertEquals(orderUpdateRequestDto.getEndRentalDate(), actual.getEndRentalDate());
        });
        assertThat(actualOrder.get().getSum()).isNotNull();
    }

    @Test
    void getById() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());
        var carResponseDto = carService.create(carRequestDto);
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var userResponseDto = userService.create(userRequestDto);
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(userResponseDto.get().getId(), carResponseDto.get().getId());
        var expectedOrder = orderService.create(orderRequestDto);

        var actualOrder = orderService.getById(expectedOrder.get().getId());

        assertThat(actualOrder).isNotNull();
        actualOrder.ifPresent(actual -> assertEquals(expectedOrder.get(), actual));
    }

    @Test
    void changeOrderStatus() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());
        var carResponseDto = carService.create(carRequestDto);
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var userResponseDto = userService.create(userRequestDto);
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(userResponseDto.get().getId(), carResponseDto.get().getId());
        var expectedOrder = orderService.create(orderRequestDto);

        var actualOrder = orderService.changeOrderStatus(expectedOrder.get().getId(), OrderStatus.DECLINED);

        assertThat(actualOrder).isNotNull();
        actualOrder.ifPresent(actual -> assertEquals(OrderStatus.DECLINED, actual.getOrderStatus()));
    }

    @Test
    void getAll() {
        var orders = orderService.getAll();

        assertThat(orders).hasSize(2);

        var startRentalTimes = orders.stream().map(OrderResponseDto::getStartRentalDate).collect(toList());
        assertThat(startRentalTimes).containsExactlyInAnyOrder(LocalDateTime.of(2022, 7, 11, 0, 0, 0),
                LocalDateTime.of(2022, 10, 1, 0, 0, 0),
                LocalDateTime.of(2022, 12, 12, 0, 0, 0));
    }

    @Test
    void getAllByStatus() {
        var expected = orderService.getById(1L);

        var actual = orderService.getAllByStatus(OrderStatus.APPROVED);

        assertThat(actual).hasSize(1);
        assertEquals(expected.get().getStartRentalDate(), actual.get(0).getStartRentalDate());
    }

    @Test
    void getAllByUserId() {
        var brandRequestDto = TestDtoBuilder.createBrandUpdateRequestDto();
        var brandResponseDto = brandService.create(brandRequestDto);
        var modelCreateRequestDto = TestDtoBuilder.createModelCreateRequestDto(brandResponseDto.get().getId());
        var modelResponseDto = modelService.create(modelCreateRequestDto);
        var categoryRequestDto = TestDtoBuilder.categoryCreateUpdateRequestDto();
        var categoryResponseDto = categoryService.create(categoryRequestDto);
        var carRequestDto = TestDtoBuilder.createCarRequestDto(brandResponseDto.get().getId(),
                modelResponseDto.get().getId(),
                categoryResponseDto.get().getId());
        var carResponseDto = carService.create(carRequestDto);
        var userRequestDto = TestDtoBuilder.createUserRequestDto();
        var userResponseDto = userService.create(userRequestDto);
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(userResponseDto.get().getId(), carResponseDto.get().getId());
        var expectedOrder = orderService.create(orderRequestDto);

        var actualOrders = orderService.getAllByUserId(expectedOrder.get().getUser().getId());

        assertThat(actualOrders).isNotNull().hasSize(1);
        assertEquals(expectedOrder.get().getStartRentalDate(), actualOrders.get(0).getStartRentalDate());
    }

    @Test
    void deleteById() {
        assertTrue(orderService.deleteById(ORDER_ID_FOR_DELETE));
    }
}