package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.dto.RetireeDto;
import pl.kurs.finaltest.dto.StudentDto;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;
import pl.kurs.finaltest.models.Student;
import pl.kurs.finaltest.repositories.PersonRepository;

import java.util.Map;

@Service
public class StudentStrategy implements PersonTypeStrategy<Student, StudentDto> {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public StudentStrategy(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Class<StudentDto> getHandledDtoClass() {
        return StudentDto.class;
    }

    @Override
    public boolean supports(PersonDto personDto) {
        return personDto instanceof StudentDto || "student".equalsIgnoreCase(personDto.getType());
    }

    @Override
    public Student addPerson(StudentDto studentDto) {
        Student student = modelMapper.map(studentDto, Student.class);
        return personRepository.save(student);
    }

    @Override
    public Student editPerson(Long id, StudentDto studentDto) {
        Student student = (Student) personRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(studentDto, student);
        return personRepository.save(student);
    }

    @Override
    public Student importFromCsvRecord(Map<String, String> csvRecord) {
        Student student = new Student();

        student.setFirstName(csvRecord.get("name"));
        student.setLastName(csvRecord.get("surname"));
        student.setPesel(csvRecord.get("PESEL number"));
        student.setEmailAddress(csvRecord.get("email address"));

        if (csvRecord.get("height") != null) {
            student.setHeight(Double.valueOf(csvRecord.get("height")));
        }
        if (csvRecord.get("weight") != null) {
            student.setWeight(Double.valueOf(csvRecord.get("weight")));
        }

        student.setUniversityName(csvRecord.get("universityName"));
        if (csvRecord.get("yearOfStudy") != null) {
            student.setYearOfStudy(Integer.valueOf(csvRecord.get("yearOfStudy")));
        }
        student.setFieldOfStudy(csvRecord.get("fieldOfStudy"));
        if (csvRecord.get("scholarship") != null) {
            student.setScholarshipAmount(Double.valueOf(csvRecord.get("scholarship")));
        }

        return personRepository.save(student);
    }
}
