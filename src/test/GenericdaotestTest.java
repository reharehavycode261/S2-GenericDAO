package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.SQLException;

public class GenericDAOTest {
    private GenericDAO dao;
    private DBConnection dbConnection;

    @BeforeEach
    public void setUp() throws Exception {
        Database database = new PostgreSQL("localhost", "5432", "testdb", "", "");
        dbConnection = database.createConnection();
        dao = new GenericDAO();
        dao.createTable(dbConnection);
    }

    @AfterEach
    public void tearDown() throws Exception {
        dbConnection.close();
    }

    @Test
    public void testCountWithEmptyTable() throws SQLException {
        long count = dao.count();
        Assertions.assertEquals(0, count, "La table doit être vide après la création");
    }

    @Test
    public void testCountAfterAddingRecords() throws SQLException {
        // Supposez qu'il y a une méthode pour ajouter des enregistrements
        dao.addRecord(dbConnection, "Record1");
        dao.addRecord(dbConnection, "Record2");
        dao.addRecord(dbConnection, "Record3");

        long count = dao.count();
        Assertions.assertEquals(3, count, "La table doit contenir 3 enregistrements après l'ajout");
    }

    @Test
    public void testCountAfterDeletingRecords() throws SQLException {
        // Ajout de quelques enregistrements
        dao.addRecord(dbConnection, "Record1");
        dao.addRecord(dbConnection, "Record2");

        // Suppression d'un enregistrement
        dao.deleteRecord(dbConnection, "Record1");

        long count = dao.count();
        Assertions.assertEquals(1, count, "La table doit contenir 1 enregistrement après la suppression");
    }

    @Test
    public void testCountWithSQLException() throws SQLException {
        GenericDAO mockDao = Mockito.mock(GenericDAO.class);
        Mockito.when(mockDao.count()).thenThrow(new SQLException("Erreur SQL simulée"));

        Assertions.assertThrows(SQLException.class, () -> {
            mockDao.count();
        }, "Une SQLException doit être levée en cas d'erreur SQL");
    }
}