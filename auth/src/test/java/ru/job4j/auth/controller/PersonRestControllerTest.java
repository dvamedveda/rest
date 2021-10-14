package ru.job4j.auth.controller;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты класса PersonRestController.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PersonRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

    /**
     * Получение всех пользователей, когда пользователей не существует.
     * @throws Exception исключения при работе контроллера.
     */
    @Test
    public void whenGetNoOneThenCorrect() throws Exception {
        this.mvc.perform(get("/person/").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Получение всех пользователей, когда пользователи существуют.
     * @throws Exception исключения при работе контроллера.
     */
    @Test
    public void whenGetAllThenCorrect() throws Exception {
        List<Person> testPersons = new ArrayList<>();
        testPersons.add(Person.of(1, "One user", "User one"));
        testPersons.add(Person.of(2, "Two user", "User two"));
        testPersons.add(Person.of(3, "Three user", "User three"));
        Mockito.when(this.personService.findAllPersons()).thenReturn(testPersons);
        this.mvc.perform(get("/person/").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].login", is("One user")))
                .andExpect(jsonPath("$[0].password", is("User one")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].login", is("Two user")))
                .andExpect(jsonPath("$[1].password", is("User two")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].login", is("Three user")))
                .andExpect(jsonPath("$[2].password", is("User three")));
    }

    /**
     * Получение конкретного пользователя.
     * @throws Exception исключения при работе контроллера.
     */
    @Test
    public void whenGetExistConcreteThenGetPersonAndHttp200() throws Exception {
        Optional<Person> testPerson = Optional.of(Person.of(4, "Four user", "User four"));
        Mockito.when(this.personService.findUserById(Mockito.eq(4))).thenReturn(testPerson);
        this.mvc.perform(get("/person/4").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.login", is("Four user")))
                .andExpect(jsonPath("$.password", is("User four")));
    }

    /**
     * Получение несуществующего пользователя.
     * @throws Exception исключения при работе контроллера.
     */
    @Test
    public void whenGetUnexistConcreteThenGetEmptyPersonAndHttp404() throws Exception {
        Optional<Person> testPerson = Optional.empty();
        Mockito.when(this.personService.findUserById(Mockito.eq(4))).thenReturn(testPerson);
        this.mvc.perform(get("/person/4").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.login", nullValue()))
                .andExpect(jsonPath("$.password", nullValue()));
    }

    /**
     * Создание нового пользователя.
     * @throws Exception исключения при работе контроллера.
     */
    @Test
    public void whenPostNewThenCreatesCorrect() throws Exception {
        String testLogin = "testCreatedLogin";
        String testPassword = "testCreatedPassword";
        Optional<Person> testPerson = Optional.of(Person.of(1, testLogin, testPassword));
        Mockito.when(this.personService.saveUser(Mockito.any())).thenReturn(testPerson);
        this.mvc.perform(post("/person/")
                .content(String.format("{\"id\": 0, \"login\": \"%s\", \"password\": \"%s\"}", testLogin, testPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        Mockito.verify(this.personService).saveUser(personCaptor.capture());
        Person savedPerson = personCaptor.getValue();
        Assert.assertThat(savedPerson.getId(), is(0));
        Assert.assertThat(savedPerson.getLogin(), is(testLogin));
        Assert.assertThat(savedPerson.getPassword(), is(testPassword));
    }

    /**
     * Обновление пользователя.
     * @throws Exception исключения при работе контроллера.
     */
    @Test
    public void whenUpdateExistUserThenUpdatesCorrect() throws Exception {
        String newLogin = "testUpdateLogin";
        String newPassword = "testUpdatePassword";
        Optional<Person> testPerson = Optional.of(Person.of(1, newLogin, newPassword));
        Mockito.when(this.personService.updateUser(Mockito.any())).thenReturn(testPerson);
        this.mvc.perform(put("/person/")
                .content(String.format("{\"id\": 1, \"login\": \"%s\", \"password\": \"%s\"}", newLogin, newPassword))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        Mockito.verify(this.personService).updateUser(personCaptor.capture());
        Person updatedPerson = personCaptor.getValue();
        Assert.assertThat(updatedPerson.getId(), is(1));
        Assert.assertThat(updatedPerson.getLogin(), is(newLogin));
        Assert.assertThat(updatedPerson.getPassword(), is(newPassword));
    }

    /**
     * Удаление пользователя.
     * @throws Exception исключения при работе контроллера.
     */
    @Test
    public void whenDeleteUserThenDeletesCorrect() throws Exception {
        Mockito.when(this.personService.deleteUser(1)).thenReturn(true);
        this.mvc.perform(delete("/person/1"))
                .andDo(print())
                .andExpect(status().isOk());
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(this.personService).deleteUser(idCaptor.capture());
        int deletePersonId = idCaptor.getValue();
        Assert.assertThat(deletePersonId, is(1));
    }
}
