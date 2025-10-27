package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Test basique de la pagination
        try {
            DBConnection dbConnection = new DBConnection();
            GenericDAO<Object> dao = new GenericDAO<>();
            
            // Supposons que nous testons avec des objets de type "Object" pour l'exemple
            List<Object> page1 = dao.fetchWithPagination(dbConnection, 10, 0);
            List<Object> page2 = dao.fetchWithPagination(dbConnection, 10, 10);
            
            System.out.println("Page 1 results: " + page1.size());
            System.out.println("Page 2 results: " + page2.size());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}