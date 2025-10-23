package test;

import database.core.GenericDAO;
import database.core.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DBConnection.getConnection();
            GenericDAO<Student> studentDAO = new GenericDAO<>(connection, "students");

            // Test de pagination
            System.out.println("=== Test Pagination ===");
            int limit = 10;
            int offset = 0;
            for (int page = 0; page < 5; page++) {
                System.out.println("Page: " + (page + 1));
                studentDAO.getPaginatedResults(limit, offset).forEach(System.out::println);
                offset += limit;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}