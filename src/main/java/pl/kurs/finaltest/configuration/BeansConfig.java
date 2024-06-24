package pl.kurs.finaltest.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurs.finaltest.annotations.PersonSubType;
import pl.kurs.finaltest.database.entity.Position;
import pl.kurs.finaltest.dto.PositionDto;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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


    @Bean
    public ObjectMapper getCustomObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String dtoPackage = "pl.kurs.finaltest.dto";

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(dtoPackage)
                .filterInputsBy(new FilterBuilder().includePackage(dtoPackage))
                .setScanners(Scanners.TypesAnnotated));

        Set<Class<?>> subtypes = reflections.getTypesAnnotatedWith(PersonSubType.class);

        for (Class<?> subType : subtypes) {
            PersonSubType annotation = subType.getAnnotation(PersonSubType.class);
            if (annotation != null) {
                String typeName = annotation.value();
                objectMapper.registerSubtypes(new NamedType(subType, typeName));
            }
        }

        return objectMapper;
    }

    @Bean
    public BlockingQueue<Runnable> importQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public ExecutorService importExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

}




