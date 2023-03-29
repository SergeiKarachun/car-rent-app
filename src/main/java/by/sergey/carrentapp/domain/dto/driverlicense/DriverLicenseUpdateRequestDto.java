package by.sergey.carrentapp.domain.dto.driverlicense;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
public class DriverLicenseUpdateRequestDto {
    @NotBlank(message = "Driver license number is required")
    @Size(min = 4, message = "Driver license number should have at least 2 characters")
    String driverLicenseNumber;
    @NotNull
    LocalDate driverLicenseIssueDate;
    @NotNull
    LocalDate driverLicenseExpirationDate;
}
