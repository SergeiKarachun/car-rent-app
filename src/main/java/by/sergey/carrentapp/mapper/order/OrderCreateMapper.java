package by.sergey.carrentapp.mapper.order;

import by.sergey.carrentapp.domain.dto.order.OrderCreateRequestDto;
import by.sergey.carrentapp.domain.entity.Car;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.domain.model.OrderStatus;
import by.sergey.carrentapp.mapper.CreateMapper;
import by.sergey.carrentapp.mapper.rentaltime.RentalTimeFromOrderCreateMapper;
import by.sergey.carrentapp.repository.CarRepository;
import by.sergey.carrentapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCreateMapper implements CreateMapper<OrderCreateRequestDto, Order> {

    private final RentalTimeFromOrderCreateMapper rentalTimeFromOrderCreateMapper;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Override
    public Order mapToEntity(OrderCreateRequestDto requestDto) {
        var rentalTime = rentalTimeFromOrderCreateMapper.mapToEntity(requestDto);
        var car = getCar(requestDto.getCarId());
        var user = getUser(requestDto.getUserId());

        var order = Order.builder()
                .date(LocalDate.now())
                .passport(requestDto.getPassport())
                .orderStatus(OrderStatus.CONFIRMATION)
                .build();

        order.setCar(car);
        order.setUser(user);
        rentalTime.setOrder(order);

        return order;
    }

    private User getUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                .orElse(null);
    }

    private Car getCar(Long carId) {
        return Optional.ofNullable(carId)
                .flatMap(carRepository::findById)
                .orElse(null);
    }
}
