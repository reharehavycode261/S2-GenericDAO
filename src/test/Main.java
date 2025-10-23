package test;

import database.core.GenericDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            GenericDAO<Money> moneyDAO = new GenericDAO<>(connection, "money", Money.class);

            // Test de la pagination
            int pageNumber = 1;
            int pageSize = 10;
            List<Money> moneyList = moneyDAO.findAllWithPagination(pageNumber, pageSize);
            
            System.out.println("Page: " + pageNumber);
            for (Money money : moneyList) {
                System.out.println("Value: " + money.getValue() + ", Name: " + money.getName());
            }

            // Tester une page suivante
            pageNumber = 2;
            moneyList = moneyDAO.findAllWithPagination(pageNumber, pageSize);
            
            System.out.println("Page: " + pageNumber);
            for (Money money : moneyList) {
                System.out.println("Value: " + money.getValue() + ", Name: " + money.getName());
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}