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

    /**
     * Met à jour les champs spécifiés d'un enregistrement dans la base de données.
     * @param fields Les champs à mettre à jour sous la forme d'une map (nom_champ -> valeur).
     * @param criteria Les critères utilisés pour sélectionner l'enregistrement à mettre à jour.
     * @throws SQLException en cas d'erreur SQL.
     */
    public void updateFields(Map<String, Object> fields, Map<String, Object> criteria) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        fields.forEach((key, value) -> sql.append(key).append(" = ?, "));
        sql.setLength(sql.length() - 2); // Supprimer la dernière virgule
        sql.append(" WHERE ");
        criteria.forEach((key, value) -> sql.append(key).append(" = ? AND "));
        sql.setLength(sql.length() - 5); // Supprimer le dernier "AND"

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                stmt.setObject(index++, entry.getValue());
            }
            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                stmt.setObject(index++, entry.getValue());
            }
            stmt.executeUpdate();
        }
    }
    
    // Méthodes supplémentaires...

    // Implémentation future : pagination des résultats
}