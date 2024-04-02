package pl.kurs.finaltest.entityspecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.models.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class SpecificationManager {


    private final Map<String, Function<SearchCriteria, Specification<Person>>> specificationFactories = new HashMap<>();

    public SpecificationManager() {
        specificationFactories.put("employee", EmployeeSpecification::new);
        specificationFactories.put("student", StudentSpecification::new);
        specificationFactories.put("retiree", RetireeSpecification::new);
    }

    public Specification<Person> getSpecification(String type, SearchCriteria criteria) {
        Function<SearchCriteria, Specification<Person>> specFactory = specificationFactories.get(type.toLowerCase());
        if (specFactory != null) {
            return specFactory.apply(criteria);
        }
        return (root, query, cb) -> cb.isTrue(cb.literal(true));
    }

}
