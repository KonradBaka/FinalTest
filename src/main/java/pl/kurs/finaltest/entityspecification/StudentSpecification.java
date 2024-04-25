package pl.kurs.finaltest.entityspecification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.StudentCriteria;
import pl.kurs.finaltest.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudentSpecification implements GenericSpecification<StudentCriteria> {


    @Override
    public Specification<Person> toSpecification(StudentCriteria criteria) {
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
                predicates.add(cb.greaterThanOrEqualTo(root.get("scholarshipAmount"), criteria.getScholarshipFrom()));
            }
            if (criteria.getScholarshipTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("scholarshipAmount"), criteria.getScholarshipTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Class<StudentCriteria> getCriteriaClass() {
        return StudentCriteria.class;
    }

//    @Override
//    public String supports() {
//        return "student";
//    }

}
