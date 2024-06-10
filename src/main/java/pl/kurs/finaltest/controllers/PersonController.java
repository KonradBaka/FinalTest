package pl.kurs.finaltest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.services.impl.PersonService;
import pl.kurs.finaltest.services.impl.SpecificationService;

import java.util.Map;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private PersonService personService;
    private SpecificationService specificationService;


    public PersonController(PersonService personService, SpecificationService specificationService) {
        this.personService = personService;
        this.specificationService = specificationService;
    }

    @GetMapping
    public ResponseEntity<Page<Person>> searchPeople(@RequestParam Map<String, String> criteria, Pageable pageable) {
        Specification<Person> spec = specificationService.createSpecification(criteria);
        Page<Person> people = personService.searchPeople(spec, pageable);
        return ResponseEntity.ok(people);
    }

    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody PersonDto personDto) {
        PersonDto createdPersonDto = personService.addPerson(personDto);
        return new ResponseEntity<>(createdPersonDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPerson(@PathVariable Long id, @RequestBody PersonDto personDto) {
        PersonDto updatedPersonDto = personService.editPerson(id, personDto);
        return ResponseEntity.ok(updatedPersonDto);
    }
}
