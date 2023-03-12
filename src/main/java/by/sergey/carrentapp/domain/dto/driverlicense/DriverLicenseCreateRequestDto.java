package by.sergey.carrentapp.domain.dto.driverlicense;

import lombok.Value;

import java.time.LocalDate;

@Value
public class DriverLicenseCreateRequestDto {
    Long userId;
    String driverLicenseNumber;
    LocalDate driverLicenseIssueDate;
    LocalDate driverLicenseExpirationDate;
}
