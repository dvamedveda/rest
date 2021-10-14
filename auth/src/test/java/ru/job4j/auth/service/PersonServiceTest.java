package ru.job4j.auth.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса PersonService.
 */
@SpringBootTest
@ActiveProfiles("test")
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    /**
     * Получение пользователей, когда пользователей не существует.
     */
    @Test
    public void whenNoUsersThenFindAllCorrect() {
        Collection<Person> persons = new ArrayList<>();
        Mockito.when(personRepository.findAll()).thenReturn(persons);
        List<Person> result = this.personService.findAllPersons();
        Assert.assertThat(result.size(), is(0));
    }

    /**
     * Получение пользователей, когда пользователи существуют.
     */
    @Test
    public void whenExistUsersThenFindAllCorrect() {
        Collection<Person> persons = new ArrayList<>();
        Person one = Person.of(1, "service user one", "one user service");
        Person two = Person.of(2, "service user two", "two user service");
        Person three = Person.of(3, "service user three", "three user service");
        persons.add(one);
        persons.add(two);
        persons.add(three);
        Mockito.when(personRepository.findAll()).thenReturn(persons);
        List<Person> result = this.personService.findAllPersons();
        Assert.assertThat(result.size(), is(3));
        Assert.assertEquals(result.get(0), one);
        Assert.assertEquals(result.get(1), two);
        Assert.assertEquals(result.get(2), three);
    }

    /**
     * Получение несуществуюещго пользователя.
     */
    @Test
    public void whenGetUnexistUserThenEmptyOptional() {
        Optional<Person> mockPerson = Optional.empty();
        Mockito.when(this.personRepository.findById(1)).thenReturn(mockPerson);
        Optional<Person> result = this.personService.findUserById(1);
        Assert.assertThat(result.isPresent(), is(false));
    }

    /**
     * Получение существующего пользователя.
     */
    @Test
    public void whenGetExistUserThenCorrect() {
        int originId = 1;
        String originLogin = "find by id";
        String originPassword = "id by find";
        Optional<Person> mockPerson = Optional.of(Person.of(originId, originLogin, originPassword));
        Mockito.when(this.personRepository.findById(1)).thenReturn(mockPerson);
        Optional<Person> result = this.personService.findUserById(1);
        Assert.assertThat(result.isPresent(), is(true));
        Person resultPerson = result.get();
        Assert.assertThat(resultPerson.getId(), is(originId));
        Assert.assertThat(resultPerson.getLogin(), is(originLogin));
        Assert.assertThat(resultPerson.getPassword(), is(originPassword));
    }

    /**
     * Сохранение пользователя.
     */
    @Test
    public void whenSaveUserThenCorrect() {
        int originId = 3;
        String originLogin = "save user";
        String originPassword = "user save";
        Person mockPerson = Person.of(originId, originLogin, originPassword);
        Mockito.when(this.personRepository.save(Mockito.any(Person.class))).thenReturn(mockPerson);
        Person savingUser = new Person();
        savingUser.setLogin("save user");
        savingUser.setPassword("user save");
        Optional<Person> resultOptional = this.personService.saveUser(savingUser);
        Assert.assertThat(resultOptional.isPresent(), is(true));
        Person result = resultOptional.get();
        Assert.assertThat(result.getId(), is(originId));
        Assert.assertThat(result.getLogin(), is(originLogin));
        Assert.assertThat(result.getPassword(), is(originPassword));
    }

    /**
     * Обновление несуществующего пользователя.
     */
    @Test
    public void whenUpdateUnexistUserThenCorrect() {
        int originId = 3;
        String originLogin = "save user";
        String originPassword = "user save";
        Person mockPerson = Person.of(originId, originLogin, originPassword);
        Mockito.when(this.personRepository.findById(3)).thenReturn(Optional.empty());
        Optional<Person> resultOptional = this.personService.updateUser(mockPerson);
        Assert.assertThat(resultOptional.isPresent(), is(false));
    }

    /**
     * Обновление существующего пользователя.
     */
    @Test
    public void whenUpdateExistUserThenCorrect() {
        int originId = 3;
        String originLogin = "save user";
        String originPassword = "user save";
        Person mockPerson = Person.of(originId, originLogin, originPassword);
        Mockito.when(this.personRepository.findById(3)).thenReturn(Optional.of(mockPerson));
        Person savingUser = new Person();
        savingUser.setId(3);
        savingUser.setLogin("save user updated");
        savingUser.setPassword("user save updated");
        Mockito.when(this.personRepository.save(savingUser)).thenReturn(savingUser);
        Optional<Person> resultOptional = this.personService.updateUser(savingUser);
        Assert.assertThat(resultOptional.isPresent(), is(true));
        Person result = resultOptional.get();
        Assert.assertThat(result.getId(), is(originId));
        Assert.assertThat(result.getLogin(), is("save user updated"));
        Assert.assertThat(result.getPassword(), is("user save updated"));
    }

    /**
     * Удаление несуществующего пользователя.
     */
    @Test
    public void whenDeleteUnexistUserThenFalse() {
        Mockito.when(this.personRepository.findById(3)).thenReturn(Optional.empty());
        Person deletePerson = Person.of(3, "delete user", "user delete");
        boolean result = this.personService.deleteUser(deletePerson.getId());
        Assert.assertFalse(result);
    }

    /**
     * Удаление существующего пользователя.
     */
    @Test
    public void whenDeleteExistUserThenTrue() {
        Person deletePerson = Person.of(3, "delete user", "user delete");
        Mockito.when(this.personRepository.findById(3)).thenReturn(Optional.of(deletePerson));
        boolean result = this.personService.deleteUser(deletePerson.getId());
        Assert.assertTrue(result);
    }
}
