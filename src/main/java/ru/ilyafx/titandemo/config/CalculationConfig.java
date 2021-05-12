package ru.ilyafx.titandemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ilyafx.titandemo.model.CalculationProperties;

@Configuration
public class CalculationConfig {

    @Bean
    @ConfigurationProperties(prefix = "calculation")
    public CalculationProperties properties() {
        return new CalculationProperties();
    }

}
