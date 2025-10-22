package database.core;

import java.sql.*;
import java.util.Map;

public abstract class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    // Méthode existante pour initialiser la connexion et les autres attributs
    // ...

    /**
     * Met à jour certains champs d'un enregistrement dans la base de données
     * @param id L'identifiant de l'enregistrement à mettre à jour
     * @param fields Les champs et valeurs à mettre à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(Object id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Le map des champs ne peut pas être vide");
        }

        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        for (String field : fields.keySet()) {
            sql.append(field).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2); // Supprimer la dernière virgule
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);

            stmt.executeUpdate();
        }
    }
    
    // ... autres méthodes déjà existantes ...
}