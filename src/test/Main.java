package test;

import database.core.Affectation;
import database.core.GenericDAO;
import database.core.DBConnection;
import database.core.Config;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = Config.getPgDb().getConnection();
            GenericDAO<?> dao = new GenericDAO<>(connection, "your_table_name");

            List<Affectation> updates = new ArrayList<>();
            updates.add(new Affectation("column1", "new_value1"));
            updates.add(new Affectation("column2", 1234));

            dao.updateFields(1, updates);

            System.out.println("Mise à jour réussie !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}