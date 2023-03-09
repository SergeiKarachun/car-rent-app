package by.sergey.carrentapp.mapper.car;

import by.sergey.carrentapp.domain.dto.car.CarResponseDto;
import by.sergey.carrentapp.domain.entity.Car;
import by.sergey.carrentapp.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class CarResponseMapper implements ResponseMapper<Car, CarResponseDto> {
    @Override
    public CarResponseDto mapToDto(Car car) {
        return CarResponseDto.builder()
                .id(car.getId())
                .brand(car.getBrand().getName())
                .model(car.getModel().getName())
                .transmission(car.getModel().getTransmission())
                .engineType(car.getModel().getEngineType())
                .color(car.getColor())
                .year(car.getYear())
                .carNumber(car.getCarNumber())
                .vin(car.getVin())
                .repaired(car.getRepaired())
                .image(car.getImage())
                .category(car.getCategory().getName())
                .price(car.getCategory().getPrice())
                .build();
    }
}
