package pl.kurs.finaltest.database.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.database.entity.Person;

public interface GenericSpecification<T extends PersonCriteria> {
    Specification<Person> toSpecification(T criteria);
    Class<T> getCriteriaClass();
}
