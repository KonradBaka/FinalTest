package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.dto.RetireeDto;
import pl.kurs.finaltest.models.*;
import pl.kurs.finaltest.repositories.PersonRepository;

import java.util.Map;

@Service
public class RetireeStrategy implements PersonTypeStrategy {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public RetireeStrategy(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean supports(PersonDto personDto) {
        return personDto instanceof RetireeDto && ((RetireeDto) personDto).getPensionAmount() != null;
    }

    @Override
    public Person addPerson(PersonDto personDto) {
        Retiree retiree = modelMapper.map(personDto, Retiree.class);
        return personRepository.save(retiree);
    }

    @Override
    public Person editPerson(Long id, PersonDto personDto) {
        Retiree retiree = (Retiree) personRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(personDto, retiree);
        return personRepository.save(retiree);
    }

    @Override
    public Person importFromCsvRecord(Map<String, String> csvRecord) {
        Retiree retiree = new Retiree();

        retiree.setFirstName(csvRecord.get("name"));
        retiree.setLastName(csvRecord.get("surname"));
        retiree.setPesel(csvRecord.get("PESEL number"));
        retiree.setEmailAddress(csvRecord.get("email address"));

        if (csvRecord.get("height") != null) {
            retiree.setHeight(Double.valueOf(csvRecord.get("height")));
        }
        if (csvRecord.get("weight") != null) {
            retiree.setWeight(Double.valueOf(csvRecord.get("weight")));
        }
        if (csvRecord.get("pensionAmount") != null) {
            retiree.setPensionAmount(Double.valueOf(csvRecord.get("pensionAmount")));
        }
        if (csvRecord.get("yearsWorked") != null) {
            retiree.setYearsWorked(Integer.valueOf(csvRecord.get("yearsWorked")));
        }

        return personRepository.save(retiree);
    }

}
