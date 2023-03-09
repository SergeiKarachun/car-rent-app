package by.sergey.carrentapp.mapper.user;

import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.entity.DriverLicense;
import by.sergey.carrentapp.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseFromUserCreateMapper implements CreateMapper<UserCreateRequestDto, DriverLicense> {
    @Override
    public DriverLicense mapToEntity(UserCreateRequestDto requestDto) {
        return DriverLicense.builder()
                .number(requestDto.driverLicenseNumber())
                .issueDate(requestDto.driverLicenseIssueDate())
                .expirationDate(requestDto.driverLicenseExpirationDate())
                .build();
    }
}
