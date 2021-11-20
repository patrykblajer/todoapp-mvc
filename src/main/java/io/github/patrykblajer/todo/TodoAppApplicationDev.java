package io.github.patrykblajer.todo;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
@EnableEncryptableProperties
@Profile("dev")
@PropertySource(name = "EncryptedProperties", value = "classpath:encrypted.properties")
@PropertySource(name = "EncryptedPropertiesDev", value = "classpath:encrypted-dev.properties")
public class TodoAppApplicationDev {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplicationDev.class, args);
    }
}