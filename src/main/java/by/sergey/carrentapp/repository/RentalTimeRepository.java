package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.RentalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalTimeRepository extends JpaRepository<RentalTime, Long>, QuerydslPredicateExecutor<RentalTime> {

    Optional<RentalTime> findByOrderId(Long id);

}
