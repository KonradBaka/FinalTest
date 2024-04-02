package pl.kurs.finaltest.entityspecification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.models.Person;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification implements Specification<Person> {

    private SearchCriteria criteria;

    public StudentSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

        if (criteria.getUniversityName() != null) {
            predicates.add(cb.like(cb.lower(root.get("universityName")), "%" + criteria.getUniversityName().toLowerCase() + "%"));
        }

        if (criteria.getFieldOfStudy() != null) {
            predicates.add(cb.like(cb.lower(root.get("fieldOfStudy")), "%" + criteria.getFieldOfStudy().toLowerCase() + "%"));
        }

        if (criteria.getYearOfStudy() != null) {
            predicates.add(cb.equal(root.get("yearOfStudy"), criteria.getYearOfStudy()));
        }

        if (criteria.getScholarshipFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("scholarship"), criteria.getScholarshipFrom()));
        }
        if (criteria.getScholarshipTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("scholarship"), criteria.getScholarshipTo()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

}
