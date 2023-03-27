package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.domain.model.OrderStatus;
import by.sergey.carrentapp.domain.projection.OrderFullView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {

    List<Order> findAllByDateBetween(LocalDate start, LocalDate end);

    @Query(value = "SELECT o " +
                   "FROM Order o " +
                   "JOIN FETCH o.car c " +
                   "WHERE upper(c.carNumber) = upper(:number)")
    List<Order> findAllByCarNumber(@Param("number") String number);

    @Query(value = "SELECT o " +
                   "FROM Order o " +
                   "JOIN FETCH o.car c " +
                   "WHERE c.id = :id")
    List<Order> findAllByCarId(@Param("id") Long id);

    @Query(value = "SELECT o " +
                   "FROM Order o " +
                   "JOIN FETCH o.user u " +
                   "WHERE u.id = :id")
    List<Order> findAllByUserId(@Param("id") Long id);

    List<Order> findAllByDate(LocalDate date);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    @Query(value = "SELECT o " +
                   "FROM Order o " +
                   "WHERE size(o.accidents) > 0 ")
    List<Order> findOrderWithAccident();

    @Query(value = "SELECT o " +
                   "FROM Order o " +
                   "WHERE o.date  >= :date")
    List<Order> findAllLimitByDate(LocalDate date);

    @Query(value = "SELECT o.id as id, " +
                   "o.date as date, " +
                   "o.order_status as orderStatus, " +
                   "o.sum as sum, " +
                   "rt.start_rental_date as startRentalTime, " +
                   "rt.end_rental_date as endRentalTime, " +
                   "c.car_number as carNumber, " +
                   "b.name as brandName, " +
                   "m.name as modelName, " +
                   "ud.name as firstname, " +
                   "ud.surname as surname, " +
                   "ud.phone as phone " +
                   "FROM orders o " +
                   "JOIN car c on o.car_id = c.id " +
                   "JOIN rental_time rt on o.id = rt.order_id " +
                   "JOIN users u on o.user_id = u.id " +
                   "JOIN user_details ud on u.id = ud.user_id " +
                   "JOIN model m on c.model_id = m.id " +
                   "JOIN brand b on c.brand_id = b.id ",
                    nativeQuery = true)
    List<OrderFullView> findAllFullView();
}
