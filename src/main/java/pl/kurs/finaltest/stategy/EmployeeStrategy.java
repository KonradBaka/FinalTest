package pl.kurs.finaltest.stategy;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.database.entity.Employee;
import pl.kurs.finaltest.database.entity.Position;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.database.repositories.PositionRepository;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.dto.PersonDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EmployeeStrategy implements PersonTypeStrategy<Employee, EmployeeDto> {

    private PositionRepository positionRepository;
    private PersonRepository personRepository;
    private ModelMapper modelMapper;


    public EmployeeStrategy(PositionRepository positionRepository, PersonRepository personRepository, ModelMapper modelMapper) {
        this.positionRepository = positionRepository;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getHandledType() {
        return "employee";
    }

    @Override
    public boolean supports(PersonDto personDto) {
        return "employee".equalsIgnoreCase(personDto.getType());
    }

    @Override
    public Employee addPerson(EmployeeDto personDto) {
        Employee employee = modelMapper.map(personDto, Employee.class);
        return personRepository.save(employee);
    }

    @Override
    public Employee editPerson(Long id, EmployeeDto employeeDto) {
        Employee employee = (Employee) personRepository.findPersonByIdWithOptymisticLock(id).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(employeeDto, employee);
        return personRepository.save(employee);
    }

    @Override
    public Employee importFromCsvRecord(Map<String, String> csvRecord) {
        AtomicBoolean isNewEmployee = new AtomicBoolean(false);
        Employee employee = personRepository.findEmployeeByPesel(csvRecord.get("pesel"))
                .orElseGet(() -> {
                    isNewEmployee.set(true);
                    return new Employee();
                });

        employee.setType(csvRecord.get("type"));
        employee.setFirstName(csvRecord.get("firstName"));
        employee.setLastName(csvRecord.get("lastName"));
        employee.setPesel(csvRecord.get("pesel"));
        employee.setEmailAddress(csvRecord.get("email"));
        employee.setHeight(csvRecord.get("height") != null ? Double.valueOf(csvRecord.get("height")) : employee.getHeight());
        employee.setWeight(csvRecord.get("weight") != null ? Double.valueOf(csvRecord.get("weight")) : employee.getWeight());
        employee.setCurrentSalary(csvRecord.get("currentSalary") != null ? Double.valueOf(csvRecord.get("currentSalary")) : employee.getCurrentSalary());

        if (csvRecord.get("employmentStartDate") != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            employee.setEmploymentStartDate(LocalDate.parse(csvRecord.get("employmentStartDate"), formatter));
        }

        employee = personRepository.saveAndFlush(employee);

        if (csvRecord.get("currentPosition") != null) {
            Position position = new Position();
            position.setEmployee(employee);
            position.setName(csvRecord.get("currentPosition"));
            position.setStartDate(employee.getEmploymentStartDate());
            position.setSalary(employee.getCurrentSalary());

            positionRepository.save(position);
            employee.setCurrentPosition(position.getName());
        } else if (isNewEmployee.get()) {
            employee.setCurrentPosition(null);
        }

        return employee;
    }
}
