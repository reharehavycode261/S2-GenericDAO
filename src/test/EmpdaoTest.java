import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import database.core.Database;

@ExtendWith(MockitoExtension.class)
public class EmpDAOTest {

    @Mock
    private Database mockDatabase;
    
    @Mock
    private ResultSet mockResultSet;
    
    private EmpDAO empDAO;

    @BeforeEach
    void setUp() {
        empDAO = new EmpDAO(mockDatabase);
    }

    @Test
    @DisplayName("Should create EmpDAO instance with database")
    void testConstructor() {
        assertNotNull(empDAO);
        assertTrue(empDAO instanceof EmpDAO);
    }

    @Test
    @DisplayName("Should create Emp entity from ResultSet")
    void testCreateEntity() throws SQLException {
        // Given
        when(mockResultSet.getString("nom")).thenReturn("Dupont");
        when(mockResultSet.getString("prenom")).thenReturn("Jean");

        // When
        Emp emp = empDAO.createEntity(mockResultSet);

        // Then
        assertNotNull(emp);
        assertEquals("Dupont", emp.getNom());
        assertEquals("Jean", emp.getPrenom());
        verify(mockResultSet).getString("nom");
        verify(mockResultSet).getString("prenom");
    }

    @Test
    @DisplayName("Should throw SQLException when ResultSet fails")
    void testCreateEntityWithSQLException() throws SQLException {
        // Given
        when(mockResultSet.getString("nom")).thenThrow(new SQLException("Database error"));

        // Then
        assertThrows(SQLException.class, () -> {
            empDAO.createEntity(mockResultSet);
        });
    }

    @Test
    @DisplayName("Should handle null values in ResultSet")
    void testCreateEntityWithNullValues() throws SQLException {
        // Given
        when(mockResultSet.getString("nom")).thenReturn(null);
        when(mockResultSet.getString("prenom")).thenReturn(null);

        // When
        Emp emp = empDAO.createEntity(mockResultSet);

        // Then
        assertNotNull(emp);
        assertNull(emp.getNom());
        assertNull(emp.getPrenom());
    }

    @Test
    @DisplayName("Should create EmpDAO with null database")
    void testConstructorWithNullDatabase() {
        assertThrows(NullPointerException.class, () -> {
            new EmpDAO(null);
        });
    }
}