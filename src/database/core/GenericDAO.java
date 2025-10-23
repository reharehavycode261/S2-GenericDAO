package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    // Constructor et autres méthodes...

    /**
     * Met à jour des champs spécifiques dans un enregistrement de la base de données.
     *
     * @param id L'identifiant de l'enregistrement à mettre à jour.
     * @param fields Les champs et leurs valeurs à mettre à jour.
     * @throws SQLException Si une erreur SQL se produit.
     */
    public void updateFields(Object id, Map<String, Object> fields) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");

        // Construction de la partie SET de la requête
        boolean first = true;
        for (String field : fields.keySet()) {
            if (!first) {
                sql.append(", ");
            }
            sql.append(field).append(" = ?");
            first = false;
        }

        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;

            // Attribution des valeurs aux paramètres
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);

            stmt.executeUpdate();
        }
    }

    // ... autres méthodes existantes ...
}