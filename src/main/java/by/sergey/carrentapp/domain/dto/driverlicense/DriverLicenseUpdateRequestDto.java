package by.sergey.carrentapp.domain.dto.driverlicense;

import java.time.LocalDate;

public record DriverLicenseUpdateRequestDto(String driverLicenseNumber,
                                            LocalDate driverLicenseIssueDate,
                                            LocalDate driverLicenseExpirationDate) {
}
