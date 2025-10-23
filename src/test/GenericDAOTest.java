package test;

import database.core.GenericDAO;
import database.core.Affectation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDAOTest {

    public static void main(String[] args) {
        try {
            // Mock de la connexion à la base de données (à remplacer par une vraie connexion)
            Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(255), age INT)");

            GenericDAO<Object> dao = new GenericDAO<>(connection, "test", "id");

            // Voir les données initiales
            statement.execute("INSERT INTO test (id, name, age) VALUES (1, 'John Doe', 30)");
            
            // Mise à jour de l'enregistrement avec l'ID 1
            List<Affectation> updates = new ArrayList<>();
            updates.add(new Affectation("name", "John Smith"));
            updates.add(new Affectation("age", 31));

            dao.updateFields(1, updates);

            // Vérification du résultat
            ResultSet rs = statement.executeQuery("SELECT * FROM test WHERE id = 1");
            if (rs.next()) {
                System.out.println("Name: " + rs.getString("name")); // Devrait afficher "John Smith"
                System.out.println("Age: " + rs.getInt("age"));      // Devrait afficher "31"
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}