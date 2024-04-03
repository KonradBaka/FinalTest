package pl.kurs.finaltest.entityspecification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.EmployeeCriteria;
import pl.kurs.finaltest.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeSpecification implements GenericSpecification<EmployeeCriteria> {

    @Override
    public Specification<Person> toSpecification(EmployeeCriteria criteria) {
        return (Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }
            if (criteria.getSurname() != null) {
                predicates.add(cb.like(cb.lower(root.get("surname")), "%" + criteria.getSurname().toLowerCase() + "%"));
            }
            if (criteria.getPesel() != null) {
                predicates.add(cb.equal(root.get("pesel"), criteria.getPesel()));
            }
            if (criteria.getEmail() != null) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + criteria.getEmail().toLowerCase() + "%"));
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
    public String supports() {
        return "employee";
    }
}
