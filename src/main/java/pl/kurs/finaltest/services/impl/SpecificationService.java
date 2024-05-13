package pl.kurs.finaltest.services.impl;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.database.entity.Employee;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.entity.Position;
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


            if (criteria.containsKey("positionsCountFrom") || criteria.containsKey("positionsCountTo")) {
                if (Employee.class.isAssignableFrom(root.getJavaType())) {
                    Join<Employee, Position> positionJoin = root.join("positions", JoinType.LEFT);
                    Expression<Long> positionsCount = criteriaBuilder.count(positionJoin.get("id"));

                    Long positionsCountFrom = Long.valueOf(criteria.getOrDefault("positionsCountFrom", "0"));
                    Long positionsCountTo = Long.valueOf(criteria.getOrDefault("positionsCountTo", String.valueOf(Long.MAX_VALUE)));

                    predicates.add(criteriaBuilder.between(positionsCount, positionsCountFrom, positionsCountTo));
                }
            }

            criteria.forEach((key, value) -> {
                String actualKey = null;
                if (key.endsWith("From")) {
                    actualKey = key.substring(0, key.length() - "From".length());
                } else if (key.endsWith("To")) {
                    actualKey = key.substring(0, key.length() - "To".length());
                }

                if (actualKey != null) {
                    Path<?> path = root.get(actualKey);

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
                    predicates.add(criteriaBuilder.equal(root.get(key), value));
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}