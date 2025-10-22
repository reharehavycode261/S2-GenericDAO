package test;

import database.core.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DBConnectionTest {
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    private DBConnection dbConnection;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() throws SQLException {
        closeable = MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        dbConnection = new DBConnection(null, mockConnection);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Test positif pour la méthode updateFields, vérifie que la requête SQL est correctement formée
     * et que les méthodes appropriées sont appelées sur le PreparedStatement.
     */
    @Test
    public void testUpdateFields() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        dbConnection.updateFields("users", fields, values, condition);

        verify(mockConnection).prepareStatement(eq("UPDATE users SET name = ?, age = ? WHERE id = 1"));
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 30);
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Test négatif pour la méthode updateFields, vérifie que SQLException est lancée
     * lorsque la préparation de la requête échoue.
     */
    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String[] fields = {"name"};
        Object[] values = {"John Doe"};
        String condition = "id = 1";

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });

        Assertions.assertEquals("Database error", exception.getMessage(), "SQLException message should match");
    }

    /**
     * Test négatif pour la méthode updateFields, vérifie que IllegalArgumentException est lancée
     * lorsque les longueurs des tableaux fields et values ne correspondent pas.
     */
    @Test
    public void testUpdateFieldsMismatchedArrays() {
        String[] fields = {"name"};
        Object[] values = {"John Doe", 30}; // Intentionally mismatched lengths
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });

        Assertions.assertEquals("Fields and values array lengths must match", exception.getMessage(), "IllegalArgumentException message should match");
    }
}