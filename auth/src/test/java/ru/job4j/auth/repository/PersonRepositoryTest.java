package ru.job4j.auth.repository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.auth.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.core.Is.is;

/**
 * Тесты репозитория PersonRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    /**
     * Сохранение пользователя.
     */
    @Test
    public void whenSaveUserThenSuccess() {
        List<Person> unexists = StreamSupport.stream(this.personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertThat(unexists.size(), is(0));
        Person savedPerson = this.personRepository.save(Person.of(0, "repo user", "user repo", 1));
        List<Person> exists = StreamSupport.stream(this.personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertThat(exists.size(), is(1));
        Person result = exists.get(0);
        Assert.assertEquals(result, savedPerson);
    }

    /**
     * Обновление пользователя.
     */
    @Test
    public void whenUpdateUserThenCorrect() {
        Person initialPerson = this.personRepository.save(Person.of(0, "test user", "user test", 2));
        List<Person> existsOne = StreamSupport.stream(this.personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertThat(existsOne.size(), is(1));
        Person resultOne = existsOne.get(0);
        Assert.assertEquals(resultOne, initialPerson);
        Person updatedPerson = this.personRepository.save(Person.of(initialPerson.getId(), "test user updated", "user test updated", 2));
        List<Person> existsTwo = StreamSupport.stream(this.personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertThat(existsTwo.size(), is(1));
        Person resultTwo = existsTwo.get(0);
        Assert.assertEquals(resultTwo, updatedPerson);
    }

    /**
     * Поиск существующего пользователя по идентификатору.
     */
    @Test
    public void whenFindExistByIdThenCorrect() {
        Person initialPerson = this.personRepository.save(Person.of(0, "find user", "user find", 3));
        Optional<Person> resultOptional = personRepository.findById(1);
        Assert.assertThat(resultOptional.isPresent(), is(true));
        Person result = resultOptional.get();
        Assert.assertEquals(result, initialPerson);

    }

    /**
     * Поиск несуществующего пользователя по идентификатору.
     */
    @Test
    public void whenFindUnexistByIdThenCorrect() {
        Person initialPerson = this.personRepository.save(Person.of(0, "find user", "user find", 4));
        Optional<Person> resultOptional = personRepository.findById(initialPerson.getId() + 100);
        Assert.assertThat(resultOptional.isPresent(), is(false));
    }

    /**
     * Удаление пользователя.
     */
    @Test
    public void whenDeletePersonThenCorrect() {
        Person initialPerson = this.personRepository.save(Person.of(0, "test user", "user test", 5));
        List<Person> existsOne = StreamSupport.stream(this.personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertThat(existsOne.size(), is(1));
        Person resultOne = existsOne.get(0);
        Assert.assertEquals(resultOne, initialPerson);
        this.personRepository.delete(resultOne);
        List<Person> existsTwo = StreamSupport.stream(this.personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertThat(existsTwo.size(), is(0));
        Assert.assertThat(this.personRepository.findById(initialPerson.getId()).isPresent(), is(false));
    }

    /**
     * Проверка поиска пользователей по идентифкатору сотрудника.
     */
    @Test
    public void whenFindByIdThenCorrect() {
        Person initialPersonOne = this.personRepository.save(Person.of(0, "test user one", "user test", 7));
        Person initialPersonTwo = this.personRepository.save(Person.of(0, "test user two", "user test", 7));
        Person initialPersonThree = this.personRepository.save(Person.of(0, "test user three", "user test", 9));
        List<Person> resultPersons = StreamSupport.stream(
                this.personRepository.findAllByEmployeeId(7).spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertThat(resultPersons.size(), is(2));
        Assert.assertThat(resultPersons.contains(initialPersonOne), is(true));
        Assert.assertThat(resultPersons.contains(initialPersonTwo), is(true));
    }
}
