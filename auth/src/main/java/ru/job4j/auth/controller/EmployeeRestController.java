package ru.job4j.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.EmployeeService;

import java.util.List;

/**
 * Рест контроллер, получающий данные от другого рест контроллера.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Получить список всех сотрудников.
     *
     * @return список объектов сотредников.
     */
    @GetMapping("/")
    public List<Employee> findAll() {
        return this.employeeService.findAll();
    }

    /**
     * Создать пользователя для сотрудника.
     *
     * @param person объект построенный из тела запроса.
     * @return объект созданного пользователя.
     */
    @PostMapping("/")
    public ResponseEntity<Person> createForEmployee(@RequestBody Person person) {
        return new ResponseEntity<>(employeeService.createPersonForEmployee(person), HttpStatus.CREATED);
    }

    /**
     * Обновить данные пользователя для сотрудника.
     *
     * @param person данные пользователя из запроса.
     * @return http 200
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateForEmployee(@RequestBody Person person) {
        this.employeeService.updatePersonForEmployee(person);
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
        this.employeeService.deletePersonForEmployee(person.getId());
        return ResponseEntity.ok().build();
    }
}
