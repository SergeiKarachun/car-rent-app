package by.sergey.carrentapp.utils.predicate;

import by.sergey.carrentapp.domain.dto.filterdto.UserDetailsFilter;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static by.sergey.carrentapp.domain.entity.QUserDetails.userDetails;

@Component
public class UserDetailsPredicateBuilder implements PredicateBuilder<Predicate, UserDetailsFilter>{
    @Override
    public Predicate build(UserDetailsFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getName(), userDetails.name::containsIgnoreCase)
                .add(requestFilter.getSurname(), userDetails.surname::containsIgnoreCase)
                .add(requestFilter.getAddress(), userDetails.userContact.address::containsIgnoreCase)
                .add(requestFilter.getPhone(), userDetails.userContact.phone::eq)
                .add(requestFilter.getBirthday(), userDetails.birthday::eq)
                .buildAnd();
    }
}
