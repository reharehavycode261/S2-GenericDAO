package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Met à jour les champs spécifiés d'un enregistrement dans la base de données.
     * @param id L'identifiant de l'enregistrement à mettre à jour.
     * @param fields Un Map contenant les noms des champs et leurs nouvelles valeurs.
     * @return true si la mise à jour a réussi, false autrement.
     * @throws SQLException en cas d'erreur SQL.
     */
    public boolean updateFields(int id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) return false;

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        List<Object> values = new ArrayList<>();
        
        for (String fieldName : fields.keySet()) {
            sql.append(fieldName).append(" = ?, ");
            values.add(fields.get(fieldName));
        }
        
        sql.delete(sql.length() - 2, sql.length()); // Remove the last comma and space
        sql.append(" WHERE id = ?");
        values.add(id);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            
            return stmt.executeUpdate() > 0;
        }
    }
}