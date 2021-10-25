package ru.job4j.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сервис для работы с сотрудниками.
 */
@Service
@Transactional
public class EmployeeService {

    /**
     * Репозиторий с данными сотрудников.
     */
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Найти всех сотрудников.
     *
     * @return список объектов всех сотрудников.
     */
    public List<Employee> findAll() {
        Iterable<Employee> employees = this.employeeRepository.findAll();
        return StreamSupport.stream(
                employees.spliterator(), false)
                .collect(Collectors.toList());
    }
}
