package pl.kurs.finaltest.services;

import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.database.entity.Person;

import java.util.Map;

public interface ISpecificationService {
    Specification<Person> createSpecification(Map<String, String> criteria);

}
