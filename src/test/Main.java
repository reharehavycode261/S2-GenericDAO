import database.core.Config;
import database.core.Database;
import database.core.GenericDAO;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            Database db = Config.getPgDb();
            GenericDAO<YourEntity> dao = new GenericDAO<>(db.getConnection(), "your_table");

            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("column1", "newValue1");
            fieldsToUpdate.put("column2", "newValue2");

            int affectedRows = dao.updateFields(1, fieldsToUpdate);
            System.out.println("Nombre de lignes mises à jour : " + affectedRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}