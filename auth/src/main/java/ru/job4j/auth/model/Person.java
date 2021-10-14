package ru.job4j.auth.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Модель данных пользователя.
 */
@Entity
@Table(name = "person")
public class Person {

    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Логин пользователя.
     */
    @Column
    private String login;

    /**
     * Пароль пользователя.
     */
    @Column
    private String password;

    public Person() {
    }

    public static Person of(int id, String login, String password) {
        Person result = new Person();
        result.id = id;
        result.login = login;
        result.password = password;
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id
                && Objects.equals(login, person.login)
                && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}
