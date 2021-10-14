package ru.job4j.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сервис для работы с объектами Person.
 */
@Service
@Transactional
public class PersonService {

    /**
     * Репозиторий объектов Person.
     */
    private final PersonRepository personRepository;

    public PersonService(PersonRepository repository) {
        this.personRepository = repository;
    }

    /**
     * Найти всех пользователей.
     *
     * @return список пользователей.
     */
    public List<Person> findAllPersons() {
        return StreamSupport.stream(
                this.personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Найти пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект пользователя.
     */
    public Optional<Person> findUserById(int id) {
        return this.personRepository.findById(id);
    }

    /**
     * Сохранение нового пользователя.
     *
     * @param person объект пользователя.
     * @return сохраненный объект пользователя.
     */
    public Optional<Person> saveUser(Person person) {
        return Optional.of(this.personRepository.save(person));
    }

    /**
     * Обновление объекта пользователя.
     *
     * @param person объект пользователя.
     * @return обновленный объект пользователя.
     */
    public Optional<Person> updateUser(Person person) {
        Optional<Person> result = Optional.empty();
        Optional<Person> updatingUser = this.personRepository.findById(person.getId());
        if (updatingUser.isPresent()) {
            result = Optional.of(this.personRepository.save(person));
        }
        return result;
    }

    /**
     * Удаление пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return флаг удаления.
     */
    public boolean deleteUser(int id) {
        boolean result = false;
        Optional<Person> deletingUser = this.personRepository.findById(id);
        if (deletingUser.isPresent()) {
            this.personRepository.delete(deletingUser.get());
            result = true;
        }
        return result;
    }
}
