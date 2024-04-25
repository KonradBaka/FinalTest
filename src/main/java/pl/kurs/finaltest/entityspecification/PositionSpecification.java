package pl.kurs.finaltest.entityspecification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.PositionCriteria;
import pl.kurs.finaltest.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PositionSpecification implements GenericSpecification<PositionCriteria> {

    @Override
    public Specification<Person> toSpecification(PositionCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }

            if (criteria.getStartDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), criteria.getStartDateTo()));
            }
            if (criteria.getEndDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("endDate"), criteria.getEndDateFrom()));
            }
            if (criteria.getEndDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), criteria.getEndDateTo()));
            }
            if (criteria.getSalaryFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salary"), criteria.getSalaryFrom()));
            }
            if (criteria.getSalaryTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salary"), criteria.getSalaryTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Class<PositionCriteria> getCriteriaClass() {
        return PositionCriteria.class;
    }

//    @Override
//    public String supports() {
//        return "position";
//    }

}
