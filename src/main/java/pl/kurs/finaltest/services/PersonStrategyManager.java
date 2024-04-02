package pl.kurs.finaltest.services;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.dto.PersonDto;

import java.util.List;

@Service
public class PersonStrategyManager {

    private final List<PersonTypeStrategy> strategies;

    public PersonStrategyManager(List<PersonTypeStrategy> strategies) {
        this.strategies = strategies;
    }

    public PersonTypeStrategy getStrategy(PersonDto personDto) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(personDto))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nieistniejący typ osoby!"));
    }
}
