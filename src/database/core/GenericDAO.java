package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {
    private final Connection connection;
    private final String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // ... autres méthodes existantes ...

    /**
     * Met à jour des champs spécifiques d'un enregistrement dans la table
     * @param id L'identifiant de l'enregistrement à mettre à jour
     * @param fields Map contenant les noms des champs et leurs nouvelles valeurs
     * @return Le nombre de lignes affectées par la mise à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public int updateFields(Object id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("La collection des champs ne doit pas être vide");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        fields.forEach((key, value) -> sql.append(key).append(" = ?, "));
        sql.delete(sql.length() - 2, sql.length()); // Supprimer la dernière virgule
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);

            return stmt.executeUpdate();
        }
    }

    // ... reste du code existant ...
}