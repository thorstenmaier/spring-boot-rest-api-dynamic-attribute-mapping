# REST API - Dynamic attribute list

The project demonstrates a solution for mapping a dynamic attribute list for a REST API using Spring Boot. The REST API
client should be able to decide independently which attributes of an existing resource it wants to change.

## Example

First, a `POST` request is used to create a person with `firstname` and `lastname`.

```
POST http://localhost:8080/person
Accept: application/json
Content-Type: application/json

{
  "firstname": "Thorsten",
  "lastname": "Maier"
}
```

After that, the first name of the created person should be changed. The client should be able to transfer only the `id`
and the
`firstname` to be changed to the server. If the `lastname` is not included in the JSON, it should not be overwritten on
the server side.

Here the corresponding `PUT` request:

```
PUT http://localhost:8080/person/1
Accept: application/json
Content-Type: application/json

{
  "id": 1,
  "firstname": "Thomas"
}
```

## Data model

The application uses a JPA entity `Person` and an associated DTO `PersonDTO` for the REST API.

```java

@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;

    // ...
}
```

In the DTO, all optional fields are wrapped with the data type `java.util.Optional`.

```java
public class PersonDto {
    private Long id;
    private Optional<String> firstname;
    private Optional<String> lastname;
    // ...
}
```

## Mapping http request to DTO in @RestController

Spring MVC is able to automatically map the `Optional` data type without further configuration. The data object is
annotated with `@RequestBody` as usual.

```java
@PutMapping("/person/{id}")
public PersonDto update(@PathVariable("id") Long id,@RequestBody PersonDto personDto){
// ...
        }
```

## Mapping DTO to JPA entity

The critical part is the mapping of the DTO into the JPA entity, because only set attributes should be taken over here.
For the mapping, the use of a mapping framework is generally recommended. In this case, the library
[MapStruct](https://mapstruct.org/) is used for this task.

MapStruct has a functionality to check for the presence of an attribute in the source
([Source presence checking](https://mapstruct.org/documentation/stable/reference/html/#source-presence-check)).

All we have to do is to add a `boolean hasProperty()` method for all optional attributes in the DTO.

```java
public boolean hasFirstname(){
        return isOptionalPresent(firstname);
        }

private boolean isOptionalPresent(Optional<?> o){
        return o!=null;
        }
```

This already achieves a large part of the problem solution. We can now implement the following process:

* Load the JPA entity using the `id`
* Mapping of all passed attributes from DTO into the JPA entity
* Saving the modified JPA entity
* Mapping the JPA entity into a new and complete DTO for the response to the client.

Here is the quick-and-dirty implementation of this flow without error handling.

```java
@PutMapping("/person/{id}")
public PersonDto update(@PathVariable("id") Long id,@RequestBody PersonDto personDto){
        Person person=personRepository.findById(id).get(); // Error handling is missing
        PersonMapper.INSTANCE.map(personDto,person);
        return personMapper.map(personRepository.save(person));
        }
```

## JPA Dynamic Update

Normally, Hibernate sends all fields in the update statement to the database during an update. Thus, regardless of the
fields set, the following statement is sent to the database:

```sql
update person
set firstname=?,
    lastname=?
where id = ?
```

The Hibernate annotation `@DynamicUpdate` can be used to customize this behavior.

```java

@Entity
@DynamicUpdate
public class Person {
    // ...
}
```

With this annotation only changed fields are sent to the database. In our case only the firstname is set:

```sql
update person
set firstname=?
where id = ?
```

This reduces the amount of work on the database. However, this means more work on the Hibernate side, since the
appropriate statement has to be created for each update.

