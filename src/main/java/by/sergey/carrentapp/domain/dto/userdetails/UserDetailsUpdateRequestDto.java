package by.sergey.carrentapp.domain.dto.userdetails;

public record UserDetailsUpdateRequestDto(String name,
                                          String surname,
                                          String address,
                                          String phone) {
}
