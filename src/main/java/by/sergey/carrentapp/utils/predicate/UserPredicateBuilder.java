package by.sergey.carrentapp.utils.predicate;

import by.sergey.carrentapp.domain.dto.filterdto.UserFilter;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static by.sergey.carrentapp.domain.entity.QUser.user;

@Component
public class UserPredicateBuilder implements PredicateBuilder<Predicate, UserFilter>{
    @Override
    public Predicate build(UserFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getUsername(), user.username::eq)
                .add(requestFilter.getEmail(), user.email::eq)
                .add(requestFilter.getName(), user.userDetails.name::containsIgnoreCase)
                .add(requestFilter.getSurname(), user.userDetails.surname::containsIgnoreCase)
                .add(requestFilter.getBirthday(), user.userDetails.birthday::eq)
                .add(requestFilter.getExpirationDate(), user.userDetails.driverLicense.expirationDate::loe)
                .buildAnd();
    }
}
