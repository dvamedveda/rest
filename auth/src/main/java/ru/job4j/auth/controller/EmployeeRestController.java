package ru.job4j.auth.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.EmployeeService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Рест контроллер, получающий данные от другого рест контроллера.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    /**
     * Рест сервис пользователей для обмена данными.
     */
    private static final String PERSON_API = "http://localhost:8080/person";

    /**
     * Объект для доступа к внешнему рест сервису.
     */
    private final RestTemplate personRest;

    public EmployeeRestController(RestTemplate restTemplate, EmployeeService employeeService) {
        this.personRest = restTemplate;
        this.employeeService = employeeService;
    }

    /**
     * Получить список всех сотрудников.
     *
     * @return список объектов сотредников.
     */
    @GetMapping("/")
    public List<Employee> findAll() {
        return this.employeeService.findAll().stream().peek(employee -> {
            List<Person> persons = this.personRest.exchange(
                    PERSON_API + "/employee/" + employee.getId(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() {
                    })
                    .getBody();
            employee.setAccounts(persons);
        }).collect(Collectors.toList());
    }

    /**
     * Создать пользователя для сотрудника.
     *
     * @param person объект построенный из тела запроса.
     * @return объект созданного пользователя.
     */
    @PostMapping("/")
    public ResponseEntity<Person> createForEmployee(@RequestBody Person person) {
        Person createdPerson = this.personRest.postForObject(PERSON_API, person, Person.class);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    /**
     * Обновить данные пользователя для сотрудника.
     *
     * @param person данные пользователя из запроса.
     * @return http 200
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateForEmployee(@RequestBody Person person) {
        this.personRest.put(PERSON_API, person);
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить пользователя для сотрудника.
     *
     * @param person объект пользователя, построенный из данных в запросе.
     * @return http 200
     */
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteForEmployee(@RequestBody Person person) {
        this.personRest.delete(PERSON_API + "/" + person.getId());
        return ResponseEntity.ok().build();
    }
}
