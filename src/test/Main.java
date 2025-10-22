package test;

import database.core.GenericDAO;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Exemple de test pour la méthode updateFields()
        try {
            GenericDAO<SomeEntity> dao = new GenericDAO<>();
            Map<String, Object> fields = new HashMap<>();
            fields.put("name", "Updated Name");
            fields.put("age", 30);
            dao.updateFields(1, fields);
            System.out.println("Update successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SomeEntity {
    // Représentation de l'entité pour les tests
}