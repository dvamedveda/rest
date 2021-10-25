package ru.job4j.auth.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * Тесты сущности Соотрудник
 */
public class EmployeeTest {

    /**
     * Проверяем, что два похожих сотрудника одинаковы.
     */
    @Test
    public void whenSameThenEquals() {
        Employee one = Employee.of(1, "one", "test", 1L, new Date(1L));
        Employee two = Employee.of(1, "one", "test", 1L, new Date(1L));
        Assert.assertEquals(one, two);
    }

    /**
     * Проверяем что два непохожих сотрудника неодинаковы.
     */
    @Test
    public void whenNotSameThenNotEquals() {
        Employee one = Employee.of(1, "one", "test", 1L, new Date(1L));
        Employee two = Employee.of(2, "one", "test", 1L, new Date(1L));
        Assert.assertNotEquals(one, two);
    }

    /**
     * Проверяем, что сотрудник неодинаков с другим объектом.
     */
    @Test
    public void whenCompareWithOtherThenNotEquals() {
        Employee one = Employee.of(1, "one", "test", 1L, new Date(1L));
        Integer some = 1;
        Assert.assertNotEquals(one, some);
    }
}
