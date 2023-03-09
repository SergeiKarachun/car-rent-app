package by.sergey.carrentapp.mapper.accident;

import by.sergey.carrentapp.domain.dto.accident.AccidentResponseDto;
import by.sergey.carrentapp.domain.entity.Accident;
import by.sergey.carrentapp.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class AccidentResponseMapper implements ResponseMapper<Accident, AccidentResponseDto> {
    @Override
    public AccidentResponseDto mapToDto(Accident accident) {
        return AccidentResponseDto.builder()
                .id(accident.getId())
                .orderId(accident.getOrder().getId())
                .accidentDate(accident.getAccidentDate())
                .brand(accident.getOrder().getCar().getBrand().getName())
                .model(accident.getOrder().getCar().getModel().getName())
                .carNumber(accident.getOrder().getCar().getCarNumber())
                .userFirstName(accident.getOrder().getUser().getUserDetails().getName())
                .userLastName(accident.getOrder().getUser().getUserDetails().getSurname())
                .description(accident.getDescription())
                .damage(accident.getDamage())
                .build();
    }
}
