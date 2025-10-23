package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import database.provider.PostgreSQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Database database = new PostgreSQL("localhost", "5432", "dao", "", "");
        DBConnection dbConnection = database.createConnection();

        // Instanciation avec une connexion fictive
        Connection connection = dbConnection.connect();
        GenericDAO<SampleEntity> genericDAO = new GenericDAO<>(connection, "sample_table");

        // Création de l'entité
        SampleEntity entity = new SampleEntity(1, "Old Name", "Old Value");

        // Champs à mettre à jour
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "New Name");
        updates.put("value", "New Value");

        try {
            int rowsAffected = genericDAO.updateFields(entity, updates);
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class SampleEntity {
    private int id;
    private String name;
    private String value;

    public SampleEntity(int id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    // Getters and setters for other fields omitted for brevity
}