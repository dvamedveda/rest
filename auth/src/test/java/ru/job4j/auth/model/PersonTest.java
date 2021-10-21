package ru.job4j.auth.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Тесты модели Person.
 */
public class PersonTest {

    /**
     * Проверка неравенства объектов.
     */
    @Test
    public void whenPersonsNotSameThenNotEquals() {
        Person one = Person.of(1, "test user", "user test", 1);
        Person two = Person.of(2, "test user", "user test", 1);
        Assert.assertNotEquals(one, two);
    }

    /**
     * Проверка неравенства пользователя и другого объекта.
     */
    @Test
    public void whenCompareWithAnyOtherObjectThenNotEquals() {
        Person one = Person.of(1, "test user", "user test", 2);
        Integer two = 1;
        Assert.assertNotEquals(one, two);
    }

    /**
     * Проверка равентсва объектов.
     */
    @Test
    public void whenSameThenHashEquals() {
        Person one = Person.of(1, "test user", "user test", 3);
        Person two = Person.of(1, "test user", "user test", 3);
        Assert.assertEquals(one, two);
        Assert.assertEquals(one.hashCode(), two.hashCode());
    }
}
