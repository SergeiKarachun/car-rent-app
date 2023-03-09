package by.sergey.carrentapp.mapper.user;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseResponseDto;
import by.sergey.carrentapp.domain.dto.user.UserResponseDto;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsResponseDto;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper implements ResponseMapper<User, UserResponseDto> {
    @Override
    public UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .userDetailsDto(UserDetailsResponseDto.builder()
                        .id(user.getUserDetails().getId())
                        .userId(user.getId())
                        .name(user.getUserDetails().getName())
                        .surname(user.getUserDetails().getSurname())
                        .address(user.getUserDetails().getUserContact().getAddress())
                        .phone(user.getUserDetails().getUserContact().getPhone())
                        .birthday(user.getUserDetails().getBirthday())
                        .registrationAt(user.getUserDetails().getRegistrationDate())
                        .build())
                .driverLicenseDto(DriverLicenseResponseDto.builder()
                        .id(user.getUserDetails().getDriverLicense().getId())
                        .userId(user.getId())
                        .driverLicenseNumber(user.getUserDetails().getDriverLicense().getNumber())
                        .driverLicenseIssueDate(user.getUserDetails().getDriverLicense().getIssueDate())
                        .driverLicenseExpirationDate(user.getUserDetails().getDriverLicense().getExpirationDate())
                        .build())
                .build();
    }
}
