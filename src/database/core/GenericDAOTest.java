package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO<Object>() {
            {
                this.connection = mockConnection;
                this.tableName = "test_table";
            }
        };
    }

    @AfterEach
    void tearDown() {
        genericDAO = null;
        mockConnection = null;
        mockPreparedStatement = null;
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        genericDAO.updateFields(1, fields);

        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(2, 30);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(3, 1);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).executeUpdate();
    }

    @Test
    void testUpdateFieldsEmptyFields() throws SQLException {
        Map<String, Object> fields = new HashMap<>();

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        });

        Mockito.verify(mockPreparedStatement, Mockito.never()).executeUpdate();
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        Mockito.doThrow(new SQLException("SQL Error")).when(mockPreparedStatement).executeUpdate();

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        });

        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(2, 1);
    }

    @Test
    void testUpdateFieldsNullId() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(null, fields);
        });
    }

    @Test
    void testUpdateFieldsNullConnection() {
        genericDAO.connection = null;
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        Assertions.assertThrows(NullPointerException.class, () -> {
            genericDAO.updateFields(1, fields);
        });
    }
}