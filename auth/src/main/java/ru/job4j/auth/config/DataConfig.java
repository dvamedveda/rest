package ru.job4j.auth.config;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Конфигурация работы с данными.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.job4j.auth.repository")
@PropertySource("classpath:application.properties")
public class DataConfig {

    /**
     * Основной источник данных.
     *
     * @param url      координаты.
     * @param driver   драйвер к бд.
     * @param username имя пользователя.
     * @param password пароль.
     * @return объект источника данных.
     */
    @Bean
    @Primary
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.driver-class-name}") String driver,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Бин для обновления базы данных при запуске контекста приложения.
     *
     * @param dataSource источник данных.
     * @return бин ликвибейс.
     */
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:liquibase-changelog.xml");
        return liquibase;
    }

    /**
     * Фабрика энтити менеджеров для репозитория.
     *
     * @param dataSource источник данных.
     * @return фабрика энтити менеджеров.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("ru.job4j.auth.model");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(adapter);
        return factoryBean;
    }
}
