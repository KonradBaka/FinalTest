package pl.kurs.finaltest.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.services.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private PersonService personService;
    private ModelMapper modelMapper;

    public PersonController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> searchPersons(PersonCriteria searchCriteria, Pageable pageable) {
        Page<Person> persons = personService.findPersons(searchCriteria, pageable);
        List<PersonDto> personDtos = persons.getContent()
                .stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(personDtos);
    }

    @PostMapping
    public ResponseEntity<PersonDto> addPerson(@RequestBody PersonDto personDto) {
        Person person = personService.addPerson(personDto);
        PersonDto createdPersonDto = new ModelMapper().map(person, PersonDto.class);
        return new ResponseEntity<>(createdPersonDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> editPerson(@PathVariable Long id, @RequestBody PersonDto personDto) {
        Person updatedPerson = personService.editPerson(id, personDto);
        PersonDto updatedPersonDto = new ModelMapper().map(updatedPerson, PersonDto.class);
        return ResponseEntity.ok(updatedPersonDto);
    }
}
