package com.zgr.notification.sender;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(classes = NotificationSenderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AppTests {
    public static PostgreSQLContainer<?> postgreSQLContainer;

    @BeforeAll
    static void setUpAll() {
        if (postgreSQLContainer == null) {
            postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                    .withExposedPorts(5432)
                    .withDatabaseName("notification-service")
                    .withUsername("postgres")
                    .withPassword("test");
            postgreSQLContainer.start();
        }
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
    }
}