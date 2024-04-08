package pl.kurs.finaltest.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    @Query("SELECT p FROM Person p WHERE p.id = :id")
    <T extends Person> Optional<T> findPersonById(@Param("id") Long id);


    @Query("SELECT p FROM Person p WHERE TYPE(p) = Employee AND p.id = :id")
    Optional<Employee> findEmployeeById(@Param("id") Long id);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Person p WHERE p.id = :id")
    <T extends Person> Optional<T> findPersonByIdWithPessimisticLock(@Param("id") Long id);


}
