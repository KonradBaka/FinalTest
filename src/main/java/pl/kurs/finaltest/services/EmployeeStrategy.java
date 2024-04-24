package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Position;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.repositories.PositionRepository;

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
    @Transactional
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

        employee = personRepository.save(employee);

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

//    @Override
//    @Transactional
//    public Employee importFromCsvRecord(Map<String, String> csvRecord) {
//
//
//        Employee employee = new Employee();
//
//        employee.setType(csvRecord.get("type"));
//        employee.setFirstName(csvRecord.get("firstName"));
//        employee.setLastName(csvRecord.get("lastName"));
//        employee.setPesel(csvRecord.get("pesel"));
//        employee.setEmailAddress(csvRecord.get("email"));
//
//        if (csvRecord.get("height") != null) {
//            employee.setHeight(Double.valueOf(csvRecord.get("height")));
//        }
//        if (csvRecord.get("weight") != null) {
//            employee.setWeight(Double.valueOf(csvRecord.get("weight")));
//        }
//
//        if (csvRecord.get("employmentStartDate") != null) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate startDate = LocalDate.parse(csvRecord.get("employmentStartDate"), formatter);
//            employee.setEmploymentStartDate(startDate);
//        }
//
//        if (csvRecord.get("currentSalary") != null) {
//            employee.setCurrentSalary(Double.valueOf(csvRecord.get("currentSalary")));
//        }
//
//        employee = personRepository.save(employee);
//
//        if (csvRecord.get("currentPosition") != null && csvRecord.get("currentSalary") != null) {
//            Position position = new Position();
//            position.setEmployee(employee);
//            position.setName(csvRecord.get("currentPosition"));
//            position.setStartDate(employee.getEmploymentStartDate());
//            position.setSalary(Double.valueOf(csvRecord.get("currentSalary")));
//
//            if (employee.getPositions() == null) {
//                employee.setPositions(new HashSet<>());
//            }
//            employee.getPositions().add(position);
//            employee.setCurrentPosition(position.getName());
//
//            positionRepository.save(position);
//        }
//
//        return employee;
//    }
}
