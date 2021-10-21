package ru.job4j.auth.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сервис для работы с сотрудниками.
 * Обменивается данными с другим рест сервисом.
 */
@Service
@Transactional
public class EmployeeService {

    /**
     * Объект для доступа к внешнему рест сервису.
     */
    private final RestTemplate personRest;

    /**
     * Репозиторий с данными сотрудников.
     */
    private final EmployeeRepository employeeRepository;

    /**
     * Рест сервис пользователей для обмена данными.
     */
    private static final String PERSON_API = "http://localhost:8080/person";

    public EmployeeService(RestTemplate restTemplate, EmployeeRepository employeeRepository) {
        this.personRest = restTemplate;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Найти всех сотрудников со всеми их пользователями.
     *
     * @return список объектов всех сотрудников.
     */
    public List<Employee> findAll() {
        return StreamSupport.stream(
                this.employeeRepository.findAll().spliterator(), false)
                .peek(employee -> {
                    List<Person> persons = this.personRest.exchange(
                            PERSON_API + "/employee/" + employee.getId(),
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() {
                            })
                            .getBody();
                    employee.setAccounts(persons);
                })
                .collect(Collectors.toList());
    }

    /**
     * Создать пользователя для сотрудника.
     *
     * @param person создаваемый пользователь.
     * @return объект созданного пользователя.
     */
    public Person createPersonForEmployee(Person person) {
        return this.personRest.postForObject(PERSON_API, person, Person.class);
    }

    /**
     * Обновить данные пользователя для сотрудника.
     *
     * @param person объект обновленного пользователя.
     */
    public void updatePersonForEmployee(Person person) {
        this.personRest.put(PERSON_API, person);
    }

    /**
     * Удалить пользователя для сотрудника.
     *
     * @param id идентификатор удаляемого пользователя.
     */
    public void deletePersonForEmployee(int id) {
        this.personRest.delete(PERSON_API + "/" + id);
    }
}
