package test;

import database.core.DBConnection;
import database.core.Database;
import database.provider.PostgreSQL;
import database.core.GenericDAO;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private static Connection connection;
    private static GenericDAO<SomeEntity> dao;

    @BeforeAll
    public static void setup() throws Exception {
        Database database = new PostgreSQL("localhost", "5432", "dao", "", "");
        DBConnection dbConnection = database.createConnection();
        connection = dbConnection.getConnection();
        dao = new GenericDAO<>(connection, "some_entity_table");
    }

    @Test
    public void testCount() throws Exception {
        long count = dao.count();
        assertTrue(count >= 0, "The count should be zero or more");
    }

    @AfterAll
    public static void teardown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}