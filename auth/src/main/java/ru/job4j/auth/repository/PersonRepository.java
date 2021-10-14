package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.auth.model.Person;

/**
 * Репозиторий объектов Person.
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
}
