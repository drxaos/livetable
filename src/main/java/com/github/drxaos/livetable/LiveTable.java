package com.github.drxaos.livetable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@Configuration
@SpringBootApplication
@EnableJdbcHttpSession
@EnableScheduling
@EnableAutoConfiguration
public class LiveTable {
    public static void main(String[] args) {
        SpringApplication.run(LiveTable.class, args);
    }
}
