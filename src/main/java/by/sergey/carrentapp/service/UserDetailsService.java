package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.filterdto.UserDetailsFilter;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsCreateRequestDto;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsResponseDto;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.mapper.userdetails.UserDetailsCreateMapper;
import by.sergey.carrentapp.mapper.userdetails.UserDetailsResponseMapper;
import by.sergey.carrentapp.mapper.userdetails.UserDetailsUpdateMapper;
import by.sergey.carrentapp.repository.UserDetailsRepository;
import by.sergey.carrentapp.repository.UserRepository;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.utils.PageableUtils;
import by.sergey.carrentapp.utils.predicate.UserDetailsPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final UserDetailsCreateMapper userDetailsCreateMapper;
    private final UserDetailsResponseMapper userDetailsResponseMapper;
    private final UserDetailsUpdateMapper userDetailsUpdateMapper;
    private final UserDetailsPredicateBuilder userDetailsPredicateBuilder;

    @Transactional
    public Optional<UserDetailsResponseDto> create(UserDetailsCreateRequestDto userDetailsCreateRequestDto) {
        var existingUser = getUserByIdOrElseThrow(userDetailsCreateRequestDto.getUserId());
        var userDetails = userDetailsCreateMapper.mapToEntity(userDetailsCreateRequestDto);
        userDetails.setUser(existingUser);

        return Optional.of(userDetails)
                .map(userDetailsRepository::save)
                .map(userDetailsResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserDetailsResponseDto> update(Long id, UserDetailsUpdateRequestDto userDetailsUpdateRequestDto) {
        var existingUserDetails = getByIdOrElseThrow(id);
        return Optional.of(userDetailsUpdateMapper.mapToEntity(userDetailsUpdateRequestDto, existingUserDetails))
                .map(userDetailsRepository::save)
                .map(userDetailsResponseMapper::mapToDto);
    }

    @Transactional
    public Boolean deleteById(Long id) {
        if (userDetailsRepository.existsById(id)) {
            userDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<UserDetailsResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userDetailsResponseMapper::mapToDto);
    }

    public Optional<UserDetailsResponseDto> getByUserId(Long userId) {
        return Optional.of(userDetailsRepository.findByUserId(userId))
                .map(userDetailsResponseMapper::mapToDto);
    }

    public Page<UserDetailsResponseDto> getAll(UserDetailsFilter userDetailsFilter, Integer page, Integer pageSize) {
        Pageable pageRequest = PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "surname");

        return userDetailsRepository.findAll(userDetailsPredicateBuilder.build(userDetailsFilter), pageRequest)
                .map(userDetailsResponseMapper::mapToDto);
    }

    public List<UserDetailsResponseDto> getAllByNameAndSurname(String name, String surname) {
        return userDetailsRepository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(name, surname).stream()
                .map(userDetailsResponseMapper::mapToDto)
                .toList();
    }

    public List<UserDetailsResponseDto> getAllByRegistrationDates(LocalDate start, LocalDate end) {
        return userDetailsRepository.findAllByRegistrationDateBetween(start, end).stream()
                .map(userDetailsResponseMapper::mapToDto)
                .toList();
    }


    private User getUserByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("User","id", id)));
    }

    private UserDetails getByIdOrElseThrow(Long id) {
        return userDetailsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("UserDetails", "id", id)));
    }
}
