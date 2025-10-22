package test;

import database.core.DBConnection;
import database.core.GenericDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            // Établir une connexion à la base de données
            Properties connectionProps = new Properties();
            connectionProps.put("user", "username");
            connectionProps.put("password", "password");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydatabase", connectionProps);

            DBConnection dbConnection = new DBConnection(null, connection);
            GenericDAO<Object> dao = new GenericDAO<>("my_table");

            // Tester updateFields
            String[] fields = {"field1", "field2"};
            Object[] values = {"newValue1", 123};
            String condition = "id = 1";

            dao.updateFields(dbConnection, fields, values, condition);

            System.out.println("Mise à jour réussie !");
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}