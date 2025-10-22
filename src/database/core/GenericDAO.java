package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    // ... autres méthodes existantes ...

    /**
     * Met à jour les champs spécifiés d'une entité basée sur les conditions données
     * @param fieldsToUpdate les champs à mettre à jour avec leurs nouvelles valeurs
     * @param conditions les conditions qui déterminent quels enregistrements mettre à jour
     * @throws SQLException si une erreur SQL se produit
     */
    public void updateFields(Map<String, Object> fieldsToUpdate, Map<String, Object> conditions) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");
        
        int count = 0;
        for (String field : fieldsToUpdate.keySet()) {
            if (count > 0) {
                sql.append(", ");
            }
            sql.append(field).append(" = ?");
            count++;
        }
        
        sql.append(" WHERE ");
        count = 0;
        
        for (String conditionField : conditions.keySet()) {
            if (count > 0) {
                sql.append(" AND ");
            }
            sql.append(conditionField).append(" = ?");
            count++;
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            
            for (Object value : fieldsToUpdate.values()) {
                stmt.setObject(index++, value);
            }
            
            for (Object value : conditions.values()) {
                stmt.setObject(index++, value);
            }
            
            stmt.executeUpdate();
        }
    }

    // ... autres méthodes existantes ...
}