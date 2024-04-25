package pl.kurs.finaltest.entityspecification;

import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.models.Person;

public interface GenericSpecification<T extends PersonCriteria> {
    Specification<Person> toSpecification(T criteria);
    Class<T> getCriteriaClass();
//    String supports();
}
