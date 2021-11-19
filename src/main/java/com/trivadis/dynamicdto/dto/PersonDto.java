package com.trivadis.dynamicdto.dto;

import java.util.Optional;

public class PersonDto {
    private Long id;
    private Optional<String> firstname;
    private Optional<String> lastname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private boolean isOptionalPresent(Optional<?> o) {
        return o != null && o.isPresent();
    }

    public Optional<String> getFirstname() {
        return firstname;
    }

    public void setFirstname(Optional<String> firstname) {
        this.firstname = firstname;
    }

    public boolean hasFirstname() {
        return isOptionalPresent(firstname);
    }

    public Optional<String> getLastname() {
        return lastname;
    }

    public void setLastname(Optional<String> lastname) {
        this.lastname = lastname;
    }

    public boolean hasLastname() {
        return isOptionalPresent(lastname);
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", firstname=" + firstname +
                ", lastname=" + lastname +
                '}';
    }
}
