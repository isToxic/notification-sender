package org.zgr.notification.sender;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = NotificationSenderApplication.class)
public abstract class NotificationSenderApplicationTests {
    public static PostgreSQLContainer<?> postgreSQLContainer;

    @BeforeAll
    static void setUp() {
        if (postgreSQLContainer == null) {
            postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                    .withExposedPorts(5432)
                    .withDatabaseName("push-gateway")
                    .withUsername("postgres")
                    .withPassword("postgres");
            postgreSQLContainer.start();
        }
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.driver-class-name", postgreSQLContainer.getDriverClassName());
    }
}
