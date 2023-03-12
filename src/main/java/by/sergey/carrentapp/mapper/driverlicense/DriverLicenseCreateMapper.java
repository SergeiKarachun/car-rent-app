package by.sergey.carrentapp.mapper.driverlicense;

import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseCreateRequestDto;
import by.sergey.carrentapp.domain.entity.DriverLicense;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.mapper.CreateMapper;
import by.sergey.carrentapp.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriverLicenseCreateMapper implements CreateMapper<DriverLicenseCreateRequestDto, DriverLicense> {

    private final UserDetailsRepository userDetailsRepository;

    private DriverLicense driverLicense;

    @Override
    public DriverLicense mapToEntity(DriverLicenseCreateRequestDto requestDto) {
        driverLicense = DriverLicense.builder()
                .number(requestDto.getDriverLicenseNumber())
                .issueDate(requestDto.getDriverLicenseIssueDate())
                .expirationDate(requestDto.getDriverLicenseExpirationDate())
                .build();

        Optional<UserDetails> userDetails = getUserDetails(requestDto.getUserId());

        userDetails.ifPresent(ud -> driverLicense.setUserDetails(ud));

        return driverLicense;


    }

    private Optional<UserDetails> getUserDetails(Long userId) {
         return Optional.ofNullable(userId)
                .flatMap(userDetailsRepository::findById);
    }
}
