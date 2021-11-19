package com.trivadis.dynamicdto.controller;

import com.trivadis.dynamicdto.dto.PersonDto;
import com.trivadis.dynamicdto.mapper.PersonMapper;
import com.trivadis.dynamicdto.model.Person;
import com.trivadis.dynamicdto.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    private PersonMapper personMapper = PersonMapper.INSTANCE;

    @PostMapping("/person")
    public PersonDto save(@RequestBody PersonDto personDto) {
        Person person = personMapper.map(personDto);
        return personMapper.map(personRepository.save(person));
    }

    @PutMapping("/person/{id}")
    public PersonDto update(@PathVariable("id") Long id, @RequestBody PersonDto personDto) {
        Person person = personRepository.findById(id).get(); // Error handling is missing
        PersonMapper.INSTANCE.map(personDto, person);
        return personMapper.map(personRepository.save(person));
    }
}
