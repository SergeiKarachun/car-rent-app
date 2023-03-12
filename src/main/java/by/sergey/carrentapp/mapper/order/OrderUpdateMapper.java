package by.sergey.carrentapp.mapper.order;

import by.sergey.carrentapp.domain.dto.order.OrderUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Car;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.domain.entity.RentalTime;
import by.sergey.carrentapp.mapper.UpdateMapper;
import by.sergey.carrentapp.repository.CarRepository;
import by.sergey.carrentapp.repository.RentalTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderUpdateMapper implements UpdateMapper<OrderUpdateRequestDto, Order> {

    private final CarRepository carRepository;
    private final RentalTimeRepository rentalTimeRepository;

    @Override
    public Order mapToEntity(OrderUpdateRequestDto dto, Order entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(OrderUpdateRequestDto dto, Order entity) {
        var car = getCar(dto.getCarId());
        var rentalTime = getRentalTime(entity.getRentalTime().getId());

        rentalTime.ifPresent(rt -> rentalTimeSetter(rt, dto, entity));

        entity.setCar(car);
    }

    private Car getCar(Long carId) {
        return Optional.ofNullable(carId)
                .flatMap(carRepository::findById)
                .orElse(null);
    }

    private Optional<RentalTime> getRentalTime(Long orderId) {
        return Optional.ofNullable(orderId)
                .flatMap(rentalTimeRepository::findById);
    }

    private void rentalTimeSetter(RentalTime rt, OrderUpdateRequestDto dto, Order entity)
    {
        rt.setStartRentalDate(dto.getStartRentalTime());
        rt.setEndRentalDate(dto.getEndRentalTime());

        rt.setOrder(entity);
    }

}
