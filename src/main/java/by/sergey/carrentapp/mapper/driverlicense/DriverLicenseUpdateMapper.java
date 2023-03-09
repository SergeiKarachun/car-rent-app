package by.sergey.carrentapp.mapper.driverlicense;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.DriverLicense;
import by.sergey.carrentapp.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseUpdateMapper implements UpdateMapper<DriverLicenseUpdateRequestDto, DriverLicense> {
    @Override
    public DriverLicense mapToEntity(DriverLicenseUpdateRequestDto dto, DriverLicense entity) {
        merge(dto,entity);
        return entity;
    }

    @Override
    public void merge(DriverLicenseUpdateRequestDto dto, DriverLicense entity) {
        entity.setNumber(dto.driverLicenseNumber());
        entity.setIssueDate(dto.driverLicenseIssueDate());
        entity.setExpirationDate(dto.driverLicenseExpirationDate());
    }
}
