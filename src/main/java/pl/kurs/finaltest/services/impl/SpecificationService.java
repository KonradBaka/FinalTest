package pl.kurs.finaltest.services.impl;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.services.ISpecificationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class SpecificationService implements ISpecificationService {


    public Specification<Person> createSpecification(Map<String, String> criteria) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

            criteria.forEach((key, value) -> {
                String actualKey = null;
                if (key.endsWith("From")) {
                    actualKey = key.substring(0, key.length() - "From".length());
                } else if (key.endsWith("To")) {
                    actualKey = key.substring(0, key.length() - "To".length());
                }

                if (actualKey != null) {
                    Path<?> path = getPath(root, actualKey);

                    if (path.getJavaType() == LocalDate.class) {
                        LocalDate dateValue = LocalDate.parse(value, formatter);
                        if (key.endsWith("From")) {
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(path.as(LocalDate.class), dateValue));
                        } else {
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(path.as(LocalDate.class), dateValue));
                        }
                    } else if (path.getJavaType() == Double.class) {
                        Double doubleValue = Double.valueOf(value);
                        if (key.endsWith("From")) {
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(path.as(Double.class), doubleValue));
                        } else {
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(path.as(Double.class), doubleValue));
                        }
                    } else if (path.getJavaType() == Integer.class) {
                        Integer integerValue = Integer.valueOf(value);
                        if (key.endsWith("From")) {
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(path.as(Integer.class), integerValue));
                        } else {
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(path.as(Integer.class), integerValue));
                        }
                    }
                } else {
                    Path<?> path = getPath(root, key);
                    predicates.add(criteriaBuilder.equal(path, value));
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Path<?> getPath(Root<?> root, String fieldName) {
        if (fieldName.contains(".")) {
            String[] parts = fieldName.split("\\.");
            Path<?> path = root;
            for (String part : parts) {
                path = path.get(part);
            }
            return path;
        } else {
            return root.get(fieldName);
        }
    }
}