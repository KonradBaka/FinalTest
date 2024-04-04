package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.repositories.PersonRepository;

import java.util.Map;

@Service
public class EmployeeStrategy implements PersonTypeStrategy<Employee, EmployeeDto> {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;


    public EmployeeStrategy(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Class<EmployeeDto> getHandledDtoClass() {
        return EmployeeDto.class;
    }

    @Override
    public boolean supports(PersonDto personDto) {
        return personDto instanceof EmployeeDto || "employee".equalsIgnoreCase(personDto.getType());
    }

    @Override
    public Employee addPerson(EmployeeDto personDto) {
        Employee employee = modelMapper.map(personDto, Employee.class);
        return personRepository.save(employee);
    }

    @Override
    public Employee editPerson(Long id, EmployeeDto employeeDto) {
        Employee employee = (Employee) personRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(employeeDto, employee);
        return personRepository.save(employee);
    }

    @Override
    public Employee importFromCsvRecord(Map<String, String> csvRecord) {
        Employee employee = new Employee();
        employee.setFirstName(csvRecord.get("name"));
        employee.setLastName(csvRecord.get("surname"));
        employee.setPesel(csvRecord.get("PESEL number"));
        employee.setEmailAddress(csvRecord.get("email address"));

        if (csvRecord.get("height") != null) {
            employee.setHeight(Double.valueOf(csvRecord.get("height")));
        }
        if (csvRecord.get("weight") != null) {
            employee.setWeight(Double.valueOf(csvRecord.get("weight")));
        }

        employee.setCurrentPosition(csvRecord.get("currentPosition"));

        if (csvRecord.get("currentSalary") != null) {
            employee.setCurrentSalary(Double.valueOf(csvRecord.get("currentSalary")));
        }

        return personRepository.save(employee);
    }
}
