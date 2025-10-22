package test;

import database.core.DBConnection;
import database.core.Database;
import database.provider.PostgreSQL;
import test.Emp;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Database database = new PostgreSQL("localhost", "5432", "dao", "", "");
        DBConnection dbConnection = database.createConnection();

        Emp emp = new Emp();
        // Mettons à jour le nom et le prénom de l'employé avec l'ID 1
        try {
            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("nom", "NouveauNom");
            fieldsToUpdate.put("prenom", "NouveauPrenom");

            emp.updateFields("1", fieldsToUpdate);

            System.out.println("Fields updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}