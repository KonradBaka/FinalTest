package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.dto.StudentDto;
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
    public String getHandledType() {
        return "student";
    }

    @Override
    public boolean supports(PersonDto personDto) {
        return "student".equalsIgnoreCase(personDto.getType());
    }

    @Override
    public Student addPerson(StudentDto studentDto) {
        Student student = modelMapper.map(studentDto, Student.class);
        return personRepository.save(student);
    }

    @Override
    public Student editPerson(Long id, StudentDto studentDto) {
        Student student = (Student) personRepository.findPersonByIdWithOptymisticLock(id).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(studentDto, student);
        return personRepository.save(student);
    }

    @Override
    @Transactional
    public Student importFromCsvRecord(Map<String, String> csvRecord) {
        Student student = new Student();

        student.setType(csvRecord.get("type"));
        student.setFirstName(csvRecord.get("firstName"));
        student.setLastName(csvRecord.get("lastName"));
        student.setPesel(csvRecord.get("pesel"));
        student.setEmailAddress(csvRecord.get("email"));

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
