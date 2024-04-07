package pl.kurs.finaltest.services;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonStrategyManager {

    private final Map<String, PersonTypeStrategy<? extends Person, ? extends PersonDto>> strategyMap = new HashMap<>();

    public PersonStrategyManager(List<PersonTypeStrategy<? extends Person, ? extends PersonDto>> strategies) {
        for (PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy : strategies) {
            strategyMap.put(strategy.getHandledType().toLowerCase(), strategy);
        }
    }

    public PersonTypeStrategy<? extends Person, ? extends PersonDto> getStrategy(PersonDto personDto) {
        String type = personDto.getType();
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Typ nie może być pusty");
        }
        PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyMap.get(type.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Nie znaleziono strategi: " + type);
        }
        return strategy;
    }
}
