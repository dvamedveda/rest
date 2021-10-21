package ru.job4j.auth.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Модель данных сотрудника.
 */
@Entity
@Table(name = "employee")
public class Employee {

    public static Employee of(int id, String firstName, String lastName, long inn, Date hired) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setInn(inn);
        employee.setHired(hired);
        return employee;
    }

    /**
     * Идентификатор сотрудника.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Имя сотрудника.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Фамилия сотрудника.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Идентификационный номер сотрудника.
     */
    @Column
    private long inn;

    /**
     * Дата найма сотрудника.
     */
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date hired;

    /**
     * Список пользователей сотрудника.
     */
    @Transient
    private List<Person> accounts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getInn() {
        return inn;
    }

    public void setInn(long inn) {
        this.inn = inn;
    }

    public Date getHired() {
        return hired;
    }

    public void setHired(Date hired) {
        this.hired = hired;
    }

    public List<Person> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Person> accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return id == employee.id
                && inn == employee.inn
                && Objects.equals(firstName, employee.firstName)
                && Objects.equals(lastName, employee.lastName)
                && Objects.equals(hired, employee.hired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, inn, hired);
    }
}
