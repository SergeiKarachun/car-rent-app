package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.Accident;
import by.sergey.carrentapp.domain.projection.AccidentFullView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface AccidentRepository extends JpaRepository<Accident, Long>, QuerydslPredicateExecutor<Accident> {

    Optional<Accident> findByOrderId(Long id);

    List<Accident> findAllByAccidentDateAfter(LocalDateTime date);

    @Query(value = "SELECT a " +
                   "FROM Accident a " +
                   "WHERE a.damage >= :damage " +
                   "ORDER BY a.damage DESC ")
    List<Accident> findAllByDamage(@Param("damage")BigDecimal damage);

    @Query(value = "SELECT a " +
                   "FROM Accident a " +
                   "JOIN FETCH a.order o " +
                   "WHERE o.id = :orderId ")
    List<Accident> findAllByOrderId(@Param("orderId") Long id);

    @Query(value = "SELECT a " +
                   "FROM Accident a " +
                   "JOIN FETCH a.order o " +
                   "JOIN FETCH o.user u " +
                   "JOIN FETCH u.userDetails ud " +
                   "WHERE lower(ud.name) = lower(:name) AND lower(ud.surname) = lower(:surname) ")
    List<Accident> findAllByNameAndSurname(@Param("name") String name, @Param("surname") String surname);

    @Query(value = "SELECT a " +
                   "FROM Accident a " +
                   "JOIN FETCH a.order o " +
                   "JOIN FETCH o.car c " +
                   "WHERE c.carNumber like %:number% " +
                   "ORDER BY a.accidentDate DESC ")
    List<Accident> findByCarNumber(@Param("number") String number);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Accident a " +
                   "SET a.damage = :damage " +
                   "WHERE a.id = :id")
    int updateDamage(@Param("damage") BigDecimal damage, @Param("id") Long id);

    @Query(value = "SELECT a.id as id, " +
                   "a.accident_date as accidentDate, " +
                   "a.description as description, " +
                   "a.damage as damage, " +
                   "o.id as orderId, " +
                   "b.name as brandName, " +
                   "m.name as modelName," +
                   "c.car_number as carNumber," +
                   "ud.name as firstname, " +
                   "ud.surname as surname "+
                   "FROM accident a " +
                   "JOIN orders o on a.order_id = o.id " +
                   "JOIN car c on o.car_id = c.id " +
                   "JOIN brand b on c.brand_id = b.id " +
                   "JOIN model m on c.model_id = m.id " +
                   "JOIN users u on o.user_id = u.id " +
                   "JOIN user_details ud on u.id = ud.user_id ",
            nativeQuery = true)
    List<AccidentFullView> findAllFull();


    @Query(value = "SELECT a.id as id, " +
                   "a.accident_date as accidentDate, " +
                   "a.description as description, " +
                   "a.damage as damage, " +
                   "o.id as orderId, " +
                   "b.name as brandName, " +
                   "m.name as modelName," +
                   "c.car_number as carNumber," +
                   "ud.name as firstname, " +
                   "ud.surname as surname "+
                   "FROM accident a " +
                   "JOIN orders o on a.order_id = o.id " +
                   "JOIN car c on o.car_id = c.id " +
                   "JOIN brand b on c.brand_id = b.id " +
                   "JOIN model m on c.model_id = m.id " +
                   "JOIN users u on o.user_id = u.id " +
                   "JOIN user_details ud on u.id = ud.user_id " +
                   "WHERE a.id = :id ",
    nativeQuery = true)
    List<AccidentFullView> findByIdFull(@Param("id") Long id);


}
