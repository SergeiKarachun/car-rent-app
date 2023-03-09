package by.sergey.carrentapp.domain.dto.driverlicense;

import java.time.LocalDate;

public record DriverLicenseCreateRequestDto(Long userId,
                                            String driverLicenseNumber,
                                            LocalDate driverLicenseIssueDate,
                                            LocalDate driverLicenseExpirationDate) {
}
