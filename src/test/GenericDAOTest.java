package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private static Database database;
    private static Connection connection;
    private GenericDAO<Emp> empDAO;

    @BeforeAll
    public static void setupDatabase() throws Exception {
        database = new PostgreSQL("localhost", "5432", "dao", "", "");
        connection = database.createConnection();
    }

    @BeforeEach
    public void setup() {
        empDAO = new GenericDAO<>(connection, "employees");
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Updated Name");
        fieldsToUpdate.put("salary", 75000);

        empDAO.updateFields(1, fieldsToUpdate);

        // Ajoutez ici des assertions pour vérifier que les champs ont été mis à jour dans la base de données.
    }

    @Test
    public void testUpdateFieldsNoFields() throws SQLException {
        empDAO.updateFields(1, new HashMap<>());  // Ne devrait pas lancer d'exception
    }

    // Annotez d'autres tests en fonction des besoins

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}