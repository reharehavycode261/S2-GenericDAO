package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    void testConstructor() {
        Assertions.assertNotNull(genericDAO.connection, "Connection should not be null");
        Assertions.assertEquals("test_table", genericDAO.tableName, "Table name should be correctly set");
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        List<Affectation> fields = new ArrayList<>();
        fields.add(new Affectation("name", "John"));
        fields.add(new Affectation("age", 30));

        genericDAO.updateFields(1, fields);

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(2)).setObject(anyInt(), any());
        verify(mockPreparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateFieldsWithEmptyFields() {
        List<Affectation> fields = new ArrayList<>();

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, fields);
        });

        Assertions.assertEquals("La liste des champs à mettre à jour ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testUpdateFieldsWithNullFields() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, null);
        });

        Assertions.assertEquals("La liste des champs à mettre à jour ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        List<Affectation> fields = new ArrayList<>();
        fields.add(new Affectation("name", "John"));

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
    }
}