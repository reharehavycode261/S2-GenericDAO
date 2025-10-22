package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private GenericDAO<Emp> empDao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        // Mocking the PostgreSQL and DBConnection
        PostgreSQL database = Mockito.mock(PostgreSQL.class);
        DBConnection dbConnection = Mockito.mock(DBConnection.class);
        connection = Mockito.mock(Connection.class);

        Mockito.when(database.createConnection()).thenReturn(dbConnection);
        Mockito.when(dbConnection.getConnection()).thenReturn(connection);

        empDao = new GenericDAO<>(connection, "Employee");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clean up resources
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testUpdateFields() throws Exception {
        // Mocking PreparedStatement and ResultSet
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);

        Map<String, Object> fields = new HashMap<>();
        fields.put("nom", "Pierre");
        String whereClause = "prenom = 'Jean'";

        empDao.updateFields(fields, whereClause);

        // Verify that the prepared statement was executed
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empDao.updateFields(new HashMap<>(), "prenom = 'Jean'");
        });

        String expectedMessage = "Fields to update cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Exception message should indicate empty fields");
    }

    @Test
    public void testUpdateFieldsWithNullWhereClause() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Map<String, Object> fields = new HashMap<>();
            fields.put("nom", "Pierre");
            empDao.updateFields(fields, null);
        });

        String expectedMessage = "Where clause cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Exception message should indicate null where clause");
    }

    @Test
    public void testUpdateFieldsWithSQLException() throws Exception {
        // Mocking PreparedStatement to throw SQLException
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doThrow(new SQLException("Database error")).when(preparedStatement).executeUpdate();

        Map<String, Object> fields = new HashMap<>();
        fields.put("nom", "Pierre");
        String whereClause = "prenom = 'Jean'";

        Exception exception = assertThrows(SQLException.class, () -> {
            empDao.updateFields(fields, whereClause);
        });

        assertEquals("Database error", exception.getMessage(), "Exception message should indicate database error");
    }
}