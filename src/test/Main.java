package test;

import database.core.GenericDAO;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Assuming Student is a table that extends GenericDAO
            GenericDAO<Student> studentDAO = new Student();

            // Test pagination
            List<Student> firstPage = studentDAO.paginate(0, 10); // Page 1, 10 items
            System.out.println("First Page: " + firstPage);

            List<Student> secondPage = studentDAO.paginate(10, 10); // Page 2, 10 items
            System.out.println("Second Page: " + secondPage);

            // Further assertions and checks can be done to ensure data correctness
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}