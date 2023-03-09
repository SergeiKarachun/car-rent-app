package by.sergey.carrentapp.mapper.driverlicense;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseResponseDto;
import by.sergey.carrentapp.domain.entity.DriverLicense;
import by.sergey.carrentapp.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseResponseMapper implements ResponseMapper<DriverLicense, DriverLicenseResponseDto> {

    @Override
    public DriverLicenseResponseDto mapToDto(DriverLicense driverLicense) {
        return DriverLicenseResponseDto.builder()
                .id(driverLicense.getId())
                .userId(driverLicense.getUserDetails().getId())
                .driverLicenseNumber(driverLicense.getNumber())
                .driverLicenseIssueDate(driverLicense.getIssueDate())
                .driverLicenseExpirationDate(driverLicense.getExpirationDate())
                .build();

    }
}
