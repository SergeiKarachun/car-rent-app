package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, QuerydslPredicateExecutor<Category> {

    Optional<Category> findByNameIgnoreCase(String name);

    List<Category> findAllByPrice(BigDecimal price);

    List<Category> findAllByPriceLessThanEqual(BigDecimal price);

    List<Category> findAllByPriceGreaterThan(BigDecimal price);

    boolean existsByNameIgnoreCase(String name);


}
