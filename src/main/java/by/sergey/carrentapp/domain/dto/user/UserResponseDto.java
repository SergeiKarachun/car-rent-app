package by.sergey.carrentapp.domain.dto.user;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseResponseDto;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsResponseDto;
import by.sergey.carrentapp.domain.model.Role;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class UserResponseDto {
    @NotNull
    Long id;
    String username;
    String email;
    Role role;
    UserDetailsResponseDto userDetailsDto;
    DriverLicenseResponseDto driverLicenseDto;
}
