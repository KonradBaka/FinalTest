package pl.kurs.finaltest.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.models.Employee;

@Configuration
public class BeansConfig {

    @Bean
    public ModelMapper createModelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper;
    }
}



