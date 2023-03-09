package by.sergey.carrentapp.domain.dto.userdetails;


import java.time.LocalDate;

public record UserDetailsCreateRequestDto(Long userId,
                                          String name,
                                          String surname,
                                          String address,
                                          String phone,
                                          LocalDate birthday) {
}
