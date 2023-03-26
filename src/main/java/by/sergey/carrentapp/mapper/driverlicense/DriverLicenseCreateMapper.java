package by.sergey.carrentapp.mapper.driverlicense;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseCreateRequestDto;
import by.sergey.carrentapp.domain.entity.DriverLicense;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.mapper.CreateMapper;
import by.sergey.carrentapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriverLicenseCreateMapper implements CreateMapper<DriverLicenseCreateRequestDto, DriverLicense> {

    private final UserRepository userRepository;

    @Override
    public DriverLicense mapToEntity(DriverLicenseCreateRequestDto requestDto) {

        return DriverLicense.builder()
                .number(requestDto.getDriverLicenseNumber())
                .issueDate(requestDto.getDriverLicenseIssueDate())
                .expirationDate(requestDto.getDriverLicenseExpirationDate())
                .userDetails(UserDetails.builder()
                        .user(getUser(requestDto.getUserId()))
                        .build())
                .build();
    }

    private User getUser(Long userId) {
         return Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                 .orElseThrow();
    }
}
