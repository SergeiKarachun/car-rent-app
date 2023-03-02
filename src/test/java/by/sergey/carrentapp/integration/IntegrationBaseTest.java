package by.sergey.carrentapp.integration;

import by.sergey.carrentapp.integration.annotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@Sql({"classpath:sql/insert_data.sql"})
@IT
public abstract class IntegrationBaseTest {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.1");


    @BeforeAll
    static void runContainer() {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
