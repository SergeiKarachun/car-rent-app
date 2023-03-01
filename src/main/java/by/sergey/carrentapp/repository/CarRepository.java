package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.Car;
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
public interface CarRepository extends JpaRepository<Car, Long>, QuerydslPredicateExecutor<Car> {

    Optional<Car> findCarByCarNumber(String carNumber);

    @Query(value = "SELECT c " +
                   "FROM Car c " +
                   "JOIN FETCH c.category cc " +
                   "WHERE cc.name = :name ")
    List<Car> findAllByCategory(String name);

    @Query(value = "SELECT c " +
                   "FROM Car c " +
                   "JOIN FETCH c.model m " +
                   "WHERE m.transmission = lower(:transmission) ")
    List<Car> findAllByTransmission(@Param("transmission") String transmission);

    @Query(value = "SELECT c " +
                   "FROM Car c " +
                   "JOIN FETCH c.orders o " +
                   "WHERE o.id = :orderId ")
    List<Car> findAllByOrderId(@Param("orderId") Long orderId);

    List<Car> findAllByYearBefore(Integer year);

    @Query(value = "SELECT c " +
                   "FROM Car c " +
                   "WHERE c.repaired = true ")
    List<Car> findAllRepaired();

    @EntityGraph(attributePaths = {"model", "brand"})
    @Query(value = "SELECT c " +
                   "FROM Car c " +
                   "WHERE c.repaired = false ")
    List<Car> findAllBroken();

    @Query(value = "SELECT count(o.id) " +
                   "FROM orders o " +
                   "JOIN car c on o.car_id = c.id " +
                   "JOIN rental_time rt on c.id = rt.car_id " +
                   "WHERE c.id = :id " +
                   "AND rt.start_rental_date NOT BETWEEN :startDate AND :endDate " +
                   "AND rt.end_rental_date NOT BETWEEN :startDate AND :endDate ",
    nativeQuery = true)
    boolean isCarAvailable(@Param("id") Long id, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
