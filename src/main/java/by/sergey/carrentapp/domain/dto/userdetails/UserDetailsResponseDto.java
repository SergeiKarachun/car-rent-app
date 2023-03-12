package by.sergey.carrentapp.domain.dto.userdetails;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserDetailsResponseDto {
    Long id;
    Long userId;
    String name;
    String surname;
    String address;
    String phone;
    LocalDate birthday;
    LocalDate registrationAt;
}
