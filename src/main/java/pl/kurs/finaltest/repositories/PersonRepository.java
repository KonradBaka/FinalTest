package pl.kurs.finaltest.repositories;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO person (id, first_name, last_name, pesel, height, weight, email_address, type) VALUES (:id, :firstName, :lastName, :pesel, :height, :weight, :emailAddress, :type)", nativeQuery = true)
    <T extends Person> void savePerson(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("pesel") String pesel, @Param("height") Double height, @Param("weight") Double weight, @Param("emailAddress") String emailAddress, @Param("type") String type);

    @Query("SELECT p FROM Person p WHERE p.id = :id")
    <T extends Person> Optional<T> findPersonById(@Param("id") Long id);


    @Query("SELECT p FROM Person p WHERE TYPE(p) = Employee AND p.id = :id")
    Optional<Employee> findEmployeeById(@Param("id") Long id);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Person p WHERE p.id = :id")
    <T extends Person> Optional<T> findPersonByIdWithPessimisticLock(@Param("id") Long id);


}
