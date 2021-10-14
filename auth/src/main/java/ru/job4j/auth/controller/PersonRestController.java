package ru.job4j.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

import java.util.List;
import java.util.Optional;

/**
 * REST-контроллер для модели Person.
 */
@RestController
@RequestMapping("/person")
public class PersonRestController {

    /**
     * Сервис для работы с моделями Person.
     */
    private final PersonService personService;

    public PersonRestController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Получение всех объектов Person.
     *
     * @return список объектов Person.
     */
    @GetMapping("/")
    public List<Person> findAll() {
        return this.personService.findAllPersons();
    }

    /**
     * Получение объекта Person по id.
     *
     * @param id идентификатор объекта Person.
     * @return объект Person.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> getUserById(@PathVariable("id") int id) {
        Optional<Person> result = this.personService.findUserById(id);
        return new ResponseEntity<>(
                result.orElse(new Person()),
                result.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Создание нового объекта Person.
     *
     * @param person объект Person, построенный из данных запроса
     * @return созданный объект Person.
     */
    @PostMapping
    public ResponseEntity<Person> createUser(@RequestBody Person person) {
        return new ResponseEntity<>(this.personService.saveUser(person).get(), HttpStatus.CREATED);
    }

    /**
     * Обновление объекта Person. Если пользователь не найден, обновления не произойдет,
     * однако в ответ все равно вернется http 200.
     *
     * @param person объект Person, построенный из данных запроса.
     * @return объект ответа http 200.
     */
    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody Person person) {
        this.personService.updateUser(person);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление объекта Person по id. Если пользователь не найден, удаления не произойдет,
     * однако в ответ все равно вернется http 200.
     *
     * @param id идентификатор Person.
     * @return объект ответа http 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
        this.personService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
