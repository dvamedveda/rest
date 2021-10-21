package ru.job4j.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация всяких сопутствующих бинов.
 */
@Configuration
public class OtherBeansConfig {

    /**
     * Бин для взаимодействия с другими rest-сервисами.
     *
     * @return объект rest template.
     */
    @Bean
    public RestTemplate personRest() {
        return new RestTemplate();
    }
}
