package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    //boolean existsByUsername(String username);

    @Query(value = "SELECT u " +
                   "FROM User u " +
                   "JOIN FETCH u.userDetails ud " +
                   "WHERE ud.userContact.phone = :phone")
    Optional<User> findByPhone(@Param("phone") String phone);

    @EntityGraph(attributePaths = {"orders"})
    @Query(value = "SELECT u " +
                   "FROM User u " +
                   "WHERE size(u.orders) > 0")
    List<User> findAllWithOrders();

    @EntityGraph(attributePaths = {"orders"})
    @Query(value = "SELECT u " +
                   "FROM User u " +
                   "WHERE size(u.orders) = 0")
    List<User> findAllWithoutOrders();

    @EntityGraph(attributePaths = {"orders"})
    @Query(value = "SELECT DISTINCT u " +
                   "FROM User u " +
                   "JOIN FETCH u.orders o " +
                   "WHERE size(o.accidents) > 0")
    List<User> findAllWithAccident();

    @Query(value = "SELECT u " +
                   "FROM User u " +
                   "JOIN FETCH u.userDetails ud " +
                   "JOIN FETCH ud.driverLicense dl " +
                   "WHERE dl.expirationDate < :expiredDate")
    List<User> findAllWithExpiredDriverLicense(@Param("expiredDate") LocalDateTime expiredDate, Pageable pageable);

}

