package ru.job4j.auth.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Тесты сервиса сотрудников.
 */
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    /**
     * Проверка поиска всех сотрудников.
     */
    @Test
    public void whenFindAllEmployeesThenCorrect() {
        List<Employee> mockEmployees = new ArrayList<>();
        Employee mockOne = Employee.of(1, "first", "name", 1L, new Date(1L));
        Employee mockTwo = Employee.of(2, "second", "name", 2L, new Date(2L));
        mockEmployees.add(mockOne);
        mockEmployees.add(mockTwo);
        Mockito.when(this.employeeRepository.findAll()).thenReturn(mockEmployees);
        List<Employee> resultEmployees = this.employeeService.findAll();
        Assert.assertThat(resultEmployees.size(), is(2));
        Assert.assertThat(resultEmployees.contains(mockOne), is(true));
        Assert.assertThat(resultEmployees.contains(mockTwo), is(true));
    }
}
