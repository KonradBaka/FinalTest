package pl.kurs.finaltest.entityspecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.models.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpecificationManager {


    private final Map<String, GenericSpecification<?>> specifications = new HashMap<>();

    public SpecificationManager(List<GenericSpecification<?>> specs) {
        specs.forEach(spec -> specifications.put(spec.supports().toLowerCase(), spec));
    }

    public Specification<Person> getSpecification(PersonCriteria criteria) {
        GenericSpecification spec = specifications.get(criteria.getClass().getSimpleName().toLowerCase());
        if (spec != null) {
            return spec.toSpecification(criteria);
        }
        return (root, query, cb) -> cb.isTrue(cb.literal(true));
    }

}
