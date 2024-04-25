package pl.kurs.finaltest.dto;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pl.kurs.finaltest.criteria.EmployeeCriteria;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.criteria.RetireeCriteria;
import pl.kurs.finaltest.criteria.StudentCriteria;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;
import pl.kurs.finaltest.models.Student;

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
