package com.trivadis.dynamicdto.mapper;

import com.trivadis.dynamicdto.dto.PersonDto;
import com.trivadis.dynamicdto.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper( PersonMapper.class );

    Person map(PersonDto personDto);

    PersonDto map(Person person);

    void map(PersonDto personDto, @MappingTarget Person person);

    default <T> T map(Optional<T> t) {
        return t != null ? t.orElse(null) : null;
    }

    default <T> Optional<T> map(T t) {
        return Optional.ofNullable(t);
    }
}