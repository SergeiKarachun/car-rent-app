package by.sergey.carrentapp.domain.dto.userdetails;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDetailsResponseDto(Long id,
                                     Long userId,
                                     String name,
                                     String surname,
                                     String address,
                                     String phone,
                                     LocalDate birthday,
                                     LocalDate registrationAt) {
}
