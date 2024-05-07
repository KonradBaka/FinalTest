package pl.kurs.finaltest.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.Retiree;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.dto.RetireeDto;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.services.PersonTypeStrategy;

import java.util.Map;

@Service
public class RetireeStrategy implements PersonTypeStrategy<Retiree, RetireeDto> {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public RetireeStrategy(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getHandledType() {
        return "retiree";
    }

    @Override
    public boolean supports(PersonDto personDto) {
        return personDto instanceof RetireeDto || "retiree".equalsIgnoreCase(personDto.getType());

    }

    @Override
    public Retiree addPerson(RetireeDto retireeDto) {
        Retiree retiree = modelMapper.map(retireeDto, Retiree.class);
        return personRepository.save(retiree);
    }

    @Override
    public Retiree editPerson(Long id, RetireeDto retireeDto) {
        Retiree retiree = (Retiree) personRepository.findPersonByIdWithOptymisticLock(id).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(retireeDto, retiree);
        return personRepository.save(retiree);
    }

    @Override
    @Transactional
    public Retiree importFromCsvRecord(Map<String, String> csvRecord) {
        Retiree retiree = new Retiree();

        retiree.setType(csvRecord.get("type"));
        retiree.setFirstName(csvRecord.get("firstName"));
        retiree.setLastName(csvRecord.get("lastName"));
        retiree.setPesel(csvRecord.get("pesel"));
        retiree.setEmailAddress(csvRecord.get("email"));

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

//        return personRepository.save(retiree);
        return retiree;
    }

}
