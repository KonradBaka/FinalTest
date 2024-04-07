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

    private final Map<Class<? extends PersonCriteria>, GenericSpecification<?>> specificationMap = new HashMap<>();

    public SpecificationManager(List<GenericSpecification<?>> specifications) {
        for (GenericSpecification<?> spec : specifications) {
            specificationMap.put(spec.getCriteriaClass(), spec);
        }
    }

    public <T extends PersonCriteria> Specification<Person> getSpecification(T criteria) {
        GenericSpecification<?> genericSpec = specificationMap.get(criteria.getClass());

        if (genericSpec != null) {
            @SuppressWarnings("unchecked")
            GenericSpecification<T> spec = (GenericSpecification<T>) genericSpec;

            return spec.toSpecification(criteria);
        }

        return (root, query, cb) -> cb.conjunction();
    }
}
