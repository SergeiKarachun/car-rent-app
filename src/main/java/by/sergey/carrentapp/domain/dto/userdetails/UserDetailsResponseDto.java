package by.sergey.carrentapp.domain.dto.userdetails;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@Builder
public class UserDetailsResponseDto {
    @NotNull
    Long id;
    @NotNull
    Long userId;
    String name;
    String surname;
    String address;
    String phone;
    LocalDate birthday;
    LocalDate registrationAt;
}
