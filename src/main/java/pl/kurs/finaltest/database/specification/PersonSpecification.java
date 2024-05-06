package pl.kurs.finaltest.database.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.database.entity.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonSpecification implements GenericSpecification<PersonCriteria>{


    @Override
    public Specification<Person> toSpecification(PersonCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getType() != null) {
                predicates.add(cb.like(cb.lower(root.get("type")), "%" + criteria.getType().toLowerCase() + "%"));
            }
            if (criteria.getFirstName() != null) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + criteria.getFirstName().toLowerCase() + "%"));
            }
            if (criteria.getLastName() != null) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + criteria.getLastName().toLowerCase() + "%"));
            }
            if (criteria.getPesel() != null) {
                predicates.add(cb.equal(root.get("pesel"), criteria.getPesel()));
            }
            if (criteria.getEmailAddress() != null) {
                predicates.add(cb.like(cb.lower(root.get("emailAddress")), "%" + criteria.getEmailAddress().toLowerCase() + "%"));
            }
            if (criteria.getHeightFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("height"), criteria.getHeightFrom()));
            }
            if (criteria.getHeightTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("height"), criteria.getHeightTo()));
            }
            if (criteria.getWeightFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("weight"), criteria.getWeightFrom()));
            }
            if (criteria.getWeightTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("weight"), criteria.getWeightTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Class<PersonCriteria> getCriteriaClass() {
        return PersonCriteria.class;
    }

}
