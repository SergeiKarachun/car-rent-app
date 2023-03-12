package by.sergey.carrentapp.domain.dto.user;

import lombok.Value;

import java.time.LocalDate;

@Value
public class UserCreateRequestDto {
    String email;
    String username;
    String password;
    String name;
    String surname;
    String address;
    String phone;
    LocalDate birthday;
    String driverLicenseNumber;
    LocalDate driverLicenseIssueDate;
    LocalDate driverLicenseExpirationDate;

}
