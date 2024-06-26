package pl.kurs.finaltest.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurs.finaltest.annotations.PersonSubType;
import pl.kurs.finaltest.database.entity.Position;
import pl.kurs.finaltest.dto.PositionDto;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class BeansConfig {

    private ApplicationContext applicationContext;


    public BeansConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Bean
    public ModelMapper createModelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.typeMap(PositionDto.class, Position.class).addMappings(mapping -> {
            mapping.skip(Position::setId);
        });

        return mapper;
    }

    @Bean
    public ObjectMapper getCustomObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(PersonSubType.class);

        for (Object bean : beansWithAnnotation.values()) {
            Class<?> subType = AopProxyUtils.ultimateTargetClass(bean);
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




