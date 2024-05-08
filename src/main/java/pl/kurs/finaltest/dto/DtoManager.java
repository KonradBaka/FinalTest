package pl.kurs.finaltest.dto;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.database.entity.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class DtoManager {

    private Map<Class<? extends Person>, Class<? extends PersonDto>> dtoMap = new HashMap<>();

    @PostConstruct
    public void init() {
        dtoMap.put(Person.class, PersonDto.class);
        dtoMap.put(Employee.class, EmployeeDto.class);
        dtoMap.put(Student.class, StudentDto.class);
        dtoMap.put(Retiree.class, RetireeDto.class);
    }

    public Class<? extends PersonDto> getDtoClass(Class<? extends Person> personClass) {
        return dtoMap.getOrDefault(personClass, PersonDto.class);
    }
}
