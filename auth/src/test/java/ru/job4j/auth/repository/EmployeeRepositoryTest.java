package ru.job4j.auth.repository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.auth.model.Employee;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.core.Is.is;

/**
 * Тесты репозитория сотрудников.
 */
@DataJpaTest
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Проверка поиска всех сотрудников.
     */
    @Test
    public void whenSearchEmployeesThenCorrect() {
        Employee employeeOne = this.employeeRepository.save(Employee.of(0, "first", "name", 1L, new Date(1L)));
        Employee employeeTwo = this.employeeRepository.save(Employee.of(0, "second", "name", 2L, new Date(2L)));
        List<Employee> resultEmployees = StreamSupport.stream(this.employeeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertThat(resultEmployees.size(), is(2));
        Assert.assertThat(resultEmployees.get(0), is(employeeOne));
        Assert.assertThat(resultEmployees.get(1), is(employeeTwo));
    }
}
