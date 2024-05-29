package com.example.languagelearning;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIT {

    private static final String flywayTestMigrationLocation = "classpath:db/migration";
    @ServiceConnection
    public static PostgreSQLContainer<?>postgreSQLContainer = new PostgreSQLContainer<>("13.1-alpine");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void configFlyway(DynamicPropertyRegistry registry) {
        registry.add("spring.flyway.locations=", AbstractIT::getFlywayTestMigrationLocation);
    }

    private static String getFlywayTestMigrationLocation() {
        return flywayTestMigrationLocation;
    }
}
