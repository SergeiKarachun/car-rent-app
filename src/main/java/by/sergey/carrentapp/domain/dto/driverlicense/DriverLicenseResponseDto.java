package by.sergey.carrentapp.domain.dto.driverlicense;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@Builder
public class DriverLicenseResponseDto {
    @NotNull
    Long id;
    @NotNull
    Long userId;
    String driverLicenseNumber;
    LocalDate driverLicenseIssueDate;
    LocalDate driverLicenseExpirationDate;
}
