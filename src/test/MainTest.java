package test;

import database.core.Affectation;
import database.core.GenericDAO;
import database.core.DBConnection;
import database.core.Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MainTest {

    private Connection mockConnection;
    private GenericDAO<?> mockDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Setup mock connection and DAO
        mockConnection = mock(Connection.class);
        mockDAO = mock(GenericDAO.class);
        
        // Mock Config to return the mock connection
        Config configMock = mock(Config.class);
        when(configMock.getPgDb()).thenReturn(new DBConnection() {
            @Override
            public Connection getConnection() {
                return mockConnection;
            }
        });
    }

    @AfterEach
    public void tearDown() {
        // Clean up resources
        mockConnection = null;
        mockDAO = null;
    }

    @Test
    public void testMainSuccessfulUpdate() throws Exception {
        // Arrange
        List<Affectation> updates = new ArrayList<>();
        updates.add(new Affectation("column1", "new_value1"));
        updates.add(new Affectation("column2", 1234));

        // Act
        Main.main(new String[]{});

        // Assert
        verify(mockDAO, times(1)).updateFields(1, updates);
        System.out.println("Mise à jour réussie !");
    }

    @Test
    public void testMainSQLException() throws Exception {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Exception exception = Assertions.assertThrows(SQLException.class, () -> {
            Main.main(new String[]{});
        });

        Assertions.assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testMainNullConnection() throws Exception {
        // Arrange
        when(Config.getPgDb().getConnection()).thenReturn(null);

        // Act & Assert
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> {
            Main.main(new String[]{});
        });

        Assertions.assertEquals("Cannot invoke \"java.sql.Connection.prepareStatement(String)\" because \"connection\" is null", exception.getMessage());
    }
}