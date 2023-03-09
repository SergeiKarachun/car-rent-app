package by.sergey.carrentapp.domain.dto.driverlicense;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DriverLicenseResponseDto(Long id,
                                       Long userId,
                                       String driverLicenseNumber,
                                       LocalDate driverLicenseIssueDate,
                                       LocalDate driverLicenseExpirationDate) {
}
