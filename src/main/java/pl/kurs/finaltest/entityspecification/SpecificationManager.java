package pl.kurs.finaltest.entityspecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.models.Person;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SpecificationManager {

    private final Map<String, GenericSpecification<? extends PersonCriteria>> specifications;

    public SpecificationManager(List<GenericSpecification<? extends PersonCriteria>> specs) {
        this.specifications = specs.stream()
                .collect(Collectors.toMap(spec -> spec.getClass().getSimpleName().toLowerCase(), Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public <T extends PersonCriteria> Specification<Person> getSpecification(T criteria) {
        GenericSpecification<T> spec = (GenericSpecification<T>) specifications.get(criteria.getClass().getSimpleName().toLowerCase());
        if (spec != null) {
            return spec.toSpecification(criteria);
        } else {
            throw new IllegalArgumentException("Nie znaleziono specyfikacji dla kryteriów: " + criteria.getClass().getSimpleName());
        }
    }
}
