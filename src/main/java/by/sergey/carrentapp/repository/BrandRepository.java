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

    Boolean existsByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"models"})
    Optional<Brand> findByNameIgnoreCase(String name);

    List<Brand> findAllByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"models"})
    List<Brand> findByNameInIgnoreCase(List<String> name);

    @Query(value = "SELECT b.id as id," +
                   "b.name as name, " +
                   "m.id as modelId, " +
                   "m.name as modelName," +
                   "m.engine_type as engineType," +
                   "m.transmission as transmission " +
                   "FROM brand b " +
                   "JOIN model m on b.id = m.brand_id ",
    nativeQuery = true)
    List<BrandFullView> findAllByFullView();

    @Query(value = "SELECT b.id as id," +
                   "b.name as name, " +
                   "m.id as modelId, " +
                   "m.name as modelName," +
                   "m.engine_type as engineType," +
                   "m.transmission as transmission " +
                   "FROM brand b " +
                   "JOIN model m on b.id = m.brand_id " +
                   "where b.id = :id",
            nativeQuery = true)
    List<BrandFullView> findFullViewById(@Param("id") Long id);

    @Query(value = "SELECT b.id as id," +
                   "b.name as name, " +
                   "m.id as modelId, " +
                   "m.name as modelName," +
                   "m.engine_type as engineType," +
                   "m.transmission as transmission " +
                   "FROM brand b " +
                   "JOIN model m on b.id = m.brand_id " +
                   "WHERE upper(b.name) LIKE upper('%'||:name||'%') ",
    nativeQuery = true)
    List<BrandFullView> findAllFullViewByName(@Param("name") String name);

}
