package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class MainTest {

    private DBConnection mockDbConnection;
    private GenericDAO<Object> mockDao;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks avant chaque test
        mockDbConnection = Mockito.mock(DBConnection.class);
        mockDao = Mockito.mock(GenericDAO.class);
    }

    @AfterEach
    void tearDown() {
        // Nettoyage après chaque test
        mockDbConnection = null;
        mockDao = null;
    }

    @Test
    void testMainPaginationSuccess() throws SQLException {
        // Préparation des données de test
        List<Object> mockPage1 = new ArrayList<>();
        List<Object> mockPage2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mockPage1.add(new Object());
            mockPage2.add(new Object());
        }

        // Configuration des comportements des mocks
        Mockito.when(mockDao.fetchWithPagination(mockDbConnection, 10, 0)).thenReturn(mockPage1);
        Mockito.when(mockDao.fetchWithPagination(mockDbConnection, 10, 10)).thenReturn(mockPage2);

        // Exécution du test
        List<Object> page1 = mockDao.fetchWithPagination(mockDbConnection, 10, 0);
        List<Object> page2 = mockDao.fetchWithPagination(mockDbConnection, 10, 10);

        // Assertions
        Assertions.assertEquals(10, page1.size(), "Page 1 should contain 10 elements");
        Assertions.assertEquals(10, page2.size(), "Page 2 should contain 10 elements");
    }

    @Test
    void testMainPaginationEmptyResults() throws SQLException {
        // Configuration des comportements des mocks pour retourner des listes vides
        Mockito.when(mockDao.fetchWithPagination(mockDbConnection, 10, 0)).thenReturn(new ArrayList<>());
        Mockito.when(mockDao.fetchWithPagination(mockDbConnection, 10, 10)).thenReturn(new ArrayList<>());

        // Exécution du test
        List<Object> page1 = mockDao.fetchWithPagination(mockDbConnection, 10, 0);
        List<Object> page2 = mockDao.fetchWithPagination(mockDbConnection, 10, 10);

        // Assertions
        Assertions.assertEquals(0, page1.size(), "Page 1 should be empty");
        Assertions.assertEquals(0, page2.size(), "Page 2 should be empty");
    }

    @Test
    void testMainPaginationSQLException() throws SQLException {
        // Configuration des comportements des mocks pour lancer une SQLException
        Mockito.when(mockDao.fetchWithPagination(mockDbConnection, 10, 0)).thenThrow(new SQLException("Database error"));

        // Exécution du test et vérification de l'exception
        Assertions.assertThrows(SQLException.class, () -> {
            mockDao.fetchWithPagination(mockDbConnection, 10, 0);
        }, "Fetching page 1 should throw SQLException");
    }
}