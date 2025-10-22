package database.core;

import java.sql.*;
import java.util.*;
import static database.core.DBTool.*;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // ... autres méthodes existantes ...

    /**
     * Met à jour les champs spécifiés de l'enregistrement correspondant dans la base de données.
     * 
     * @param id L'identifiant de l'enregistrement à mettre à jour.
     * @param fields Map des champs et de leurs nouvelles valeurs.
     * @throws SQLException En cas d'erreur SQL.
     */
    public void updateFields(long id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) return;

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (String fieldName : fields.keySet()) {
            sql.append(fieldName).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2);  // Supprimer la dernière virgule
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setLong(index, id);
            stmt.executeUpdate();
        }
    }

    // ... reste du code existant ...
}