package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.domain.model.Transmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long>, QuerydslPredicateExecutor<Model> {

    Optional<Model> findModelByNameIgnoreCase(String name);

    @Query(value = "SELECT m " +
                   "FROM Model m " +
                   "JOIN FETCH m.brand b " +
                   "WHERE upper(b.name) = upper(:name) ")
    List<Model> findModelsByBrandName(@Param("name") String name);

    @Query(value = "SELECT m " +
                   "FROM Model m " +
                   "JOIN FETCH m.brand b " +
                   "WHERE b.id = :id ")
    List<Model> findModelsByBrandId(@Param("id") Long id);

    List<Model> findAllByTransmission(Transmission transmission);

}
