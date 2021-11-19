package com.trivadis.dynamicdto.repository;

import com.trivadis.dynamicdto.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
