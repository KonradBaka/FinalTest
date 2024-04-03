package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.dto.StudentDto;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Student;
import pl.kurs.finaltest.repositories.PersonRepository;

import java.util.Map;

@Service
public class StudentStrategy implements PersonTypeStrategy {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public StudentStrategy(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean supports(PersonDto personDto) {
        return "student".equalsIgnoreCase(personDto.getType());
    }

    @Override
    public Person addPerson(PersonDto personDto) {
        Student student = modelMapper.map(personDto, Student.class);
        return personRepository.save(student);
    }

    @Override
    public Person editPerson(Long id, PersonDto personDto) {
        Student student = (Student) personRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(personDto, student);
        return personRepository.save(student);
    }

    @Override
    public Person importFromCsvRecord(Map<String, String> csvRecord) {
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
