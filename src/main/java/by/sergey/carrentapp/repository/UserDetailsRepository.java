package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>, QuerydslPredicateExecutor<UserDetails> {

    UserDetails findByUserId(Long userId);

    List<UserDetails> findAllByRegistrationDateBetween(LocalDate start, LocalDate end);

    List<UserDetails> findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String name, String surname);

    @Query(value = "SELECT ud " +
                   "FROM user_details ud " +
                   "WHERE ud.userContact.phone = :phone ")
    Optional<UserDetails> findByPhone(@Param("phone") String phone);

}
