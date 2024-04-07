package pl.kurs.finaltest.entityspecification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.criteria.PositionCriteria;
import pl.kurs.finaltest.criteria.RetireeCriteria;
import pl.kurs.finaltest.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class RetireeSpecification implements GenericSpecification<RetireeCriteria> {

    @Override
    public Specification<Person> toSpecification(RetireeCriteria criteria) {
        return (Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (criteria.getFirstName() != null) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + criteria.getFirstName().toLowerCase() + "%"));
            }
            if (criteria.getLastName() != null) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + criteria.getLastName().toLowerCase() + "%"));
            }
            if (criteria.getPesel() != null) {
                predicates.add(cb.equal(root.get("pesel"), criteria.getPesel()));
            }
            if (criteria.getFirstName() != null) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + criteria.getLastName().toLowerCase() + "%"));
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

    @Override
    public String supports() {
        return "retiree";
    }
}
