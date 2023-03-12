package by.sergey.carrentapp.mapper.userdetails;

import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.UserContact;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsUpdateMapper implements UpdateMapper<UserDetailsUpdateRequestDto, UserDetails> {
    @Override
    public UserDetails mapToEntity(UserDetailsUpdateRequestDto dto, UserDetails entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(UserDetailsUpdateRequestDto dto, UserDetails entity) {
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setUserContact(UserContact.builder()
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .build());
    }
}
