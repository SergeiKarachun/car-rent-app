package by.sergey.carrentapp.mapper.rentaltime;

import by.sergey.carrentapp.domain.dto.rentaltime.RentalTimeUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.RentalTime;
import by.sergey.carrentapp.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class RentalTimeUpdateMapper implements UpdateMapper<RentalTimeUpdateRequestDto, RentalTime> {
    @Override
    public RentalTime mapToEntity(RentalTimeUpdateRequestDto dto, RentalTime entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(RentalTimeUpdateRequestDto dto, RentalTime entity) {
        entity.setStartRentalDate(dto.startRentalTime());
        entity.setEndRentalDate(dto.endRentalTime());
    }
}
