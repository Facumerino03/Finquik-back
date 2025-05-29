package com.finquik;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDatabaseConnectivity() {
        assertNotNull(jdbcTemplate, "JdbcTemplate should not be null. Verify your database configuration.");

        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertEquals(1, result, "The test query to the database should return 1.");
            System.out.println("Database connection and test query successful in DatabaseConnectionTest!");
        } catch (Exception e) {
            fail("Failed to connect to the database or execute the test query: " + e.getMessage());
        }
    }
}
