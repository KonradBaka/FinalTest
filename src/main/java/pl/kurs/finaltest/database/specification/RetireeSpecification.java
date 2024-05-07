package pl.kurs.finaltest.database.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.RetireeCriteria;
import pl.kurs.finaltest.database.entity.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class RetireeSpecification implements GenericSpecification<RetireeCriteria> {

    @Override
    public Specification<Person> toSpecification(RetireeCriteria criteria) {
        return (Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (criteria.getPensionAmountFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pensionAmount"), criteria.getPensionAmountFrom()));
            }
            if (criteria.getPensionAmountTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pensionAmount"), criteria.getPensionAmountTo()));
            }
            if (criteria.getYearsWorkedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("yearsWorked"), criteria.getYearsWorkedFrom()));
            }
            if (criteria.getYearsWorkedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("yearsWorked"), criteria.getYearsWorkedTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Class<RetireeCriteria> getCriteriaClass() {
        return RetireeCriteria.class;
    }

}
