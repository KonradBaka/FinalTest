package pl.kurs.finaltest.database.specification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.database.entity.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpecificationManager {

    private Map<Class<? extends PersonCriteria>, GenericSpecification<?>> specificationMap = new HashMap<>();

    @Autowired
    public SpecificationManager(List<GenericSpecification<?>> specifications) {
        for (GenericSpecification<?> spec : specifications) {
            specificationMap.put(spec.getCriteriaClass(), spec);
        }
    }

    public <T extends PersonCriteria> Specification<Person> getSpecification(T criteria) {
        GenericSpecification<T> specification = (GenericSpecification<T>) specificationMap.get(criteria.getClass());
        if (specification == null) {
            throw new InvalidInputData("Nie znaleziono specyfikacji dla typu: " + criteria.getClass().getSimpleName());
        }
        return specification.toSpecification(criteria);
    }
}
