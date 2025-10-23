package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.*;

public class MainTest {

    private GenericDAO<PlatConso> platConsoDAO;
    private Connection connection;
    private DBConnection dbConnectionMock;

    @BeforeEach
    public void setUp() throws Exception {
        // Mocking DBConnection and Connection
        dbConnectionMock = mock(DBConnection.class);
        connection = mock(Connection.class);
        when(dbConnectionMock.getConnection()).thenReturn(connection);

        platConsoDAO = new GenericDAO<PlatConso>(connection, "plat_conso") {
            @Override
            protected PlatConso mapResultSetToEntity(ResultSet rs) throws SQLException {
                return new PlatConso(rs.getString("platId"), rs.getString("empId"), rs.getDate("localDate").toLocalDate(), rs.getString("file"));
            }
        };
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clean up resources
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testFindAllPaginated() throws Exception {
        // Given
        int page = 1;
        int pageSize = 10;
        ResultSet resultSetMock = mock(ResultSet.class);

        // Mocking ResultSet behavior
        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getString("platId")).thenReturn("1", "2");
        when(resultSetMock.getString("empId")).thenReturn("emp1", "emp2");
        when(resultSetMock.getDate("localDate")).thenReturn(java.sql.Date.valueOf("2023-01-01"), java.sql.Date.valueOf("2023-01-02"));
        when(resultSetMock.getString("file")).thenReturn("file1", "file2");

        // Mocking DAO behavior
        platConsoDAO = Mockito.spy(platConsoDAO);
        doReturn(resultSetMock).when(platConsoDAO).executeQuery(anyString());

        // When
        List<PlatConso> paginatedList = platConsoDAO.findAllPaginated(page, pageSize);

        // Then
        Assertions.assertFalse(paginatedList.isEmpty(), "La liste paginée ne doit pas être vide");
        Assertions.assertTrue(paginatedList.size() <= pageSize, "La taille de la liste paginée ne doit pas dépasser la taille de la page");
    }

    @Test
    public void testFindAllPaginated_EmptyResult() throws Exception {
        // Given
        int page = 1;
        int pageSize = 10;
        ResultSet resultSetMock = mock(ResultSet.class);

        // Mocking ResultSet behavior for empty result
        when(resultSetMock.next()).thenReturn(false);

        // Mocking DAO behavior
        platConsoDAO = Mockito.spy(platConsoDAO);
        doReturn(resultSetMock).when(platConsoDAO).executeQuery(anyString());

        // When
        List<PlatConso> paginatedList = platConsoDAO.findAllPaginated(page, pageSize);

        // Then
        Assertions.assertTrue(paginatedList.isEmpty(), "La liste paginée doit être vide");
    }

    @Test
    public void testFindAllPaginated_InvalidPageSize() {
        // Given
        int page = 1;
        int pageSize = -1;

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            platConsoDAO.findAllPaginated(page, pageSize);
        }, "Une exception IllegalArgumentException doit être lancée pour une taille de page invalide");
    }

    @Test
    public void testFindAllPaginated_InvalidPageNumber() {
        // Given
        int page = -1;
        int pageSize = 10;

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            platConsoDAO.findAllPaginated(page, pageSize);
        }, "Une exception IllegalArgumentException doit être lancée pour un numéro de page invalide");
    }
}