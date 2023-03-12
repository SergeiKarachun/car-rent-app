package by.sergey.carrentapp.domain.dto.driverlicense;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class DriverLicenseResponseDto {
    Long id;
    Long userId;
    String driverLicenseNumber;
    LocalDate driverLicenseIssueDate;
    LocalDate driverLicenseExpirationDate;
}
