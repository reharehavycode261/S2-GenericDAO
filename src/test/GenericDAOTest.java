package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private GenericDAO genericDAO;
    private DBConnection dbConnection;

    @BeforeEach
    public void setUp() {
        genericDAO = new GenericDAO();
        dbConnection = mock(DBConnection.class);
    }

    @Test
    public void testUpdateFields() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Updated Name");
        fieldsToUpdate.put("age", 30);

        genericDAO.updateFields(dbConnection, "example_table", "1", fieldsToUpdate);

        // Vérification que la méthode est appelée correctement avec les bons paramètres
        verify(dbConnection, times(1)).getConnection();
    }
}