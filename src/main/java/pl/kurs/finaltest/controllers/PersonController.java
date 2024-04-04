package pl.kurs.finaltest.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.services.PersonService;

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
    public ResponseEntity<Page<PersonDto>> searchPersons(@ModelAttribute PersonCriteria searchCriteria, Pageable pageable) {
        Page<PersonDto> personDtos = personService.findPersons(searchCriteria, pageable);
        return ResponseEntity.ok(personDtos);
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
