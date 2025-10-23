package test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainTest {

    private Connection mockConnection;
    private GenericDAO<Money> mockMoneyDAO;
    private MockedStatic<DriverManager> driverManagerMockedStatic;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mock the DriverManager to return a mock connection
        driverManagerMockedStatic = Mockito.mockStatic(DriverManager.class);
        mockConnection = mock(Connection.class);
        driverManagerMockedStatic.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);

        // Mock the GenericDAO
        mockMoneyDAO = mock(GenericDAO.class);
    }

    @AfterEach
    public void tearDown() {
        driverManagerMockedStatic.close();
    }

    @Test
    @DisplayName("Test main method with valid pagination")
    public void testMainWithValidPagination() throws SQLException {
        // Setup mock data
        List<Money> mockMoneyListPage1 = new ArrayList<>();
        mockMoneyListPage1.add(new Money(100, "Dollar"));
        mockMoneyListPage1.add(new Money(200, "Euro"));

        List<Money> mockMoneyListPage2 = new ArrayList<>();
        mockMoneyListPage2.add(new Money(300, "Yen"));

        when(mockMoneyDAO.findAllWithPagination(1, 10)).thenReturn(mockMoneyListPage1);
        when(mockMoneyDAO.findAllWithPagination(2, 10)).thenReturn(mockMoneyListPage2);

        // Execute the main method
        Main.main(new String[]{});

        // Verify the interactions
        verify(mockMoneyDAO).findAllWithPagination(1, 10);
        verify(mockMoneyDAO).findAllWithPagination(2, 10);
    }

    @Test
    @DisplayName("Test main method with SQLException")
    public void testMainWithSQLException() throws SQLException {
        // Setup mock to throw SQLException
        driverManagerMockedStatic.when(() -> DriverManager.getConnection(anyString())).thenThrow(new SQLException("Connection failed"));

        // Capture the output
        Exception exception = assertThrows(SQLException.class, () -> {
            Main.main(new String[]{});
        });

        // Assert exception message
        assertEquals("Connection failed", exception.getMessage());
    }

    @Test
    @DisplayName("Test main method with empty result set")
    public void testMainWithEmptyResultSet() throws SQLException {
        // Setup mock data
        List<Money> emptyMoneyList = new ArrayList<>();

        when(mockMoneyDAO.findAllWithPagination(1, 10)).thenReturn(emptyMoneyList);

        // Execute the main method
        Main.main(new String[]{});

        // Verify the interaction
        verify(mockMoneyDAO).findAllWithPagination(1, 10);
    }
}