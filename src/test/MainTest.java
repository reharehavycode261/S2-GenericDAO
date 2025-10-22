package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import database.core.DBConnection;
import database.core.Database;
import database.provider.PostgreSQL;
import java.util.HashMap;
import java.util.Map;

class MainTest {

    private Database mockDatabase;
    private DBConnection mockDBConnection;
    private Emp mockEmp;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        mockDatabase = Mockito.mock(PostgreSQL.class);
        mockDBConnection = Mockito.mock(DBConnection.class);
        mockEmp = Mockito.mock(Emp.class);

        // Mock the behavior of the database connection
        Mockito.when(mockDatabase.createConnection()).thenReturn(mockDBConnection);
    }

    @AfterEach
    void tearDown() {
        // Reset the mocks after each test
        Mockito.reset(mockDatabase, mockDBConnection, mockEmp);
    }

    @Test
    void testMainMethodUpdatesFieldsSuccessfully() throws Exception {
        // Arrange
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("nom", "NouveauNom");
        fieldsToUpdate.put("prenom", "NouveauPrenom");

        // Act
        Main.main(new String[]{});

        // Assert
        Mockito.verify(mockEmp).updateFields("1", fieldsToUpdate);
    }

    @Test
    void testMainMethodHandlesExceptionGracefully() throws Exception {
        // Arrange
        Mockito.doThrow(new RuntimeException("Database error")).when(mockEmp).updateFields(Mockito.anyString(), Mockito.anyMap());

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> Main.main(new String[]{}), "Main method should handle exceptions gracefully");
    }
}