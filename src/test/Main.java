package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Main {

    private GenericDAO<PlatConso> platConsoDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        // initialisation de la connexion et configuration
        connection = new DBConnection(...).getConnection();
        platConsoDAO = new GenericDAO<PlatConso>(connection, "plat_conso") {
            @Override
            protected PlatConso mapResultSetToEntity(ResultSet rs) {
                // mapping spécifique à PlatConso
                return new PlatConso(rs.getString("platId"), rs.getString("empId"), rs.getDate("localDate").toLocalDate(), rs.getString("file"));
            }
        };
    }

    @Test
    public void testFindAllPaginated() throws Exception {
        int page = 1;
        int pageSize = 10;
        List<PlatConso> paginatedList = platConsoDAO.findAllPaginated(page, pageSize);
        
        assertFalse(paginatedList.isEmpty(), "La liste paginée ne doit pas être vide");
        assertTrue(paginatedList.size() <= pageSize, "La taille de la liste paginée ne doit pas dépasser la taille de la page");
    }
    
    // ... autres tests ...
}