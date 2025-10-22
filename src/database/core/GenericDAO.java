package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    private final Connection connection;
    private final String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }
    
    // ... autres méthodes existantes ...

    /**
     * Met à jour les champs spécifiés d'un enregistrement dans la table.
     * @param id L'identifiant de l'enregistrement à mettre à jour
     * @param fieldsToUpdate Un map des colonnes à mettre à jour avec leurs nouvelles valeurs
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(int id, Map<String, Object> fieldsToUpdate) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Aucun champ fourni pour la mise à jour.");
        }
        
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        
        int index = 0;
        for (String column : fieldsToUpdate.keySet()) {
            query.append(column).append(" = ?");
            if (index < fieldsToUpdate.size() - 1) {
                query.append(", ");
            }
            index++;
        }
        query.append(" WHERE id = ?");
        
        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            index = 1;
            for (Object value : fieldsToUpdate.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setInt(index, id);

            stmt.executeUpdate();
        }
    }
}