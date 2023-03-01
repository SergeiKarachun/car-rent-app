package by.sergey.carrentapp.repository;

import by.sergey.carrentapp.domain.entity.DriverLicense;
import by.sergey.carrentapp.domain.projection.DriverLicenseFullView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Long>, QuerydslPredicateExecutor<DriverLicense> {

    Optional<DriverLicense> findByNumberIgnoreCase(String number);

    @Query(value = "SELECT dl " +
                   "FROM DriverLicense dl " +
                   "JOIN FETCH dl.userDetails ud " +
                   "JOIN FETCH ud.user u " +
                   "WHERE u.id = :id ")
    Optional<DriverLicense> findByUserId(@Param("id") Long id);

    List<DriverLicense> findByExpirationDateLessThanEqual(LocalDateTime expirationDate);

    @Query(value = "SELECT dl.id as id, " +
                   "dl.number as number, " +
                   "dl.issue_date as issueDate, " +
                   "dl.expiration_date as expirationDate, " +
                   "ud.name as firstname, " +
                   "ud.surname as surname, " +
                   "ud.phone as phone " +
                   "FROM driver_license dl " +
                   "JOIN user_details ud on dl.user_details_id = ud.id " +
                   "WHERE dl.expiration_date <= :date ",
    nativeQuery = true)
    List<DriverLicenseFullView> findDriverLicenseFullViewExpirationDateLess(@Param("date") LocalDateTime expirationDate);

}
