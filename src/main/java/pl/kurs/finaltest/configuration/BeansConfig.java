package pl.kurs.finaltest.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurs.finaltest.database.entity.Position;
import pl.kurs.finaltest.dto.PositionDto;

@Configuration
public class BeansConfig {

    @Bean
    public ModelMapper createModelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.typeMap(PositionDto.class, Position.class).addMappings(mapping -> {
            mapping.skip(Position::setId);
        });

        return mapper;
    }
}



