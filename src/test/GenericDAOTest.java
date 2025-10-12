package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void testCount() throws SQLException {
        long count = dao.count();
        assertEquals(0, count, "La table doit être vide après la création");

        // Supposez qu'il y a une façon d'ajouter des enregistrements pour tester
        // ajouterEnregistrementsTest();

        count = dao.count();
        // Exemple: Supposons que 5 enregistrements aient été ajoutés
        // assertEquals(5, count, "La table doit contenir 5 enregistrements");
    }
}