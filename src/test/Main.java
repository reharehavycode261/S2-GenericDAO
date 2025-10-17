package test;

import database.core.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Assurez-vous de changer le Config pour le type de DB que vous utilisez
            Database db = Config.getOracleDb();
            Connection connection = db.getConnection();

            GenericDAO<Emp> empDao = new GenericDAO<>(connection, "emp");

            // Compte sans condition
            long totalEmps = empDao.count();
            System.out.println("Total Employees: " + totalEmps);

            // Compte avec condition
            long countCondition = empDao.count("salary > 5000");
            System.out.println("Employees with salary > 5000: " + countCondition);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}