package pl.kurs.finaltest.database.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.EmployeeCriteria;
import pl.kurs.finaltest.database.entity.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeSpecification implements GenericSpecification<EmployeeCriteria> {

    @Override
    public Specification<Person> toSpecification(EmployeeCriteria criteria) {
        return (Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (criteria.getStartDateOfEmploymentFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDateOfEmployment"), criteria.getStartDateOfEmploymentFrom()));
            }
            if (criteria.getStartDateOfEmploymentTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDateOfEmployment"), criteria.getStartDateOfEmploymentTo()));
            }
            if (criteria.getCurrentPosition() != null) {
                predicates.add(cb.like(cb.lower(root.get("currentPosition")), "%" + criteria.getCurrentPosition().toLowerCase() + "%"));
            }
            if (criteria.getCurrentSalaryFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("currentSalary"), criteria.getCurrentSalaryFrom()));
            }
            if (criteria.getCurrentSalaryTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("currentSalary"), criteria.getCurrentSalaryTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Class<EmployeeCriteria> getCriteriaClass() {
        return EmployeeCriteria.class;
    }

}
