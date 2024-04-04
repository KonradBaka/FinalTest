package pl.kurs.finaltest.services;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonStrategyManager {

    private final Map<Class<? extends PersonDto>, PersonTypeStrategy<? extends Person, ? extends PersonDto>> strategyMap;

    public PersonStrategyManager(List<PersonTypeStrategy<? extends Person, ? extends PersonDto>> strategies) {
        strategyMap = new HashMap<>();
        for (PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy : strategies) {
            strategyMap.put(strategy.getHandledDtoClass(), strategy);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends PersonDto> PersonTypeStrategy<? extends Person, T> getStrategy(T personDto) {
        return (PersonTypeStrategy<? extends Person, T>) strategyMap.get(personDto.getClass());
    }
}
