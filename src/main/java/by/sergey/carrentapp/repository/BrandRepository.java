package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.domain.projection.BrandFullView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, QuerydslPredicateExecutor<Brand> {

    Optional<Brand> findByName(String name);

    @EntityGraph(attributePaths = {"models", "cars"})
    Optional<Brand> findByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"models", "cars"})
    List<Brand> findByNameInIgnoreCase(List<String> name);

    @Query(value = "SELECT b " +
                   "FROM Brand b " +
                   "JOIN FETCH b.models " +
                   "JOIN FETCH b.cars ")
    List<BrandFullView> findAllByFullView();

    @Query(value = "SELECT b " +
                   "FROM Brand b " +
                   "JOIN FETCH b.models " +
                   "JOIN FETCH b.cars " +
                   "WHERE b.id = :id")
    Optional<BrandFullView> findFullViewById(@Param("id") Long id);

    @Query(value = "SELECT b " +
                   "FROM Brand b " +
                   "JOIN FETCH b.models " +
                   "JOIN FETCH b.cars " +
                   "WHERE lower(b.name) LIKE lower(:name) ")
    List<BrandFullView> findAllFullViewByName(@Param("name") String name);


}
