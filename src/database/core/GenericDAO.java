package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    // ... autres méthodes et code existant ...

    /**
     * Met à jour les champs spécifiés d'une entité dans la table.
     * @param id L'identifiant de l'entité à mettre à jour.
     * @param fields Les champs et leurs nouvelles valeurs.
     * @throws SQLException en cas d'erreur SQL.
     */
    public void updateFields(Object id, Map<String, Object> fields) throws SQLException {
        // Construction de la partie SET de la requête SQL
        StringBuilder setClause = new StringBuilder();
        for (String field : fields.keySet()) {
            if (setClause.length() != 0) {
                setClause.append(", ");
            }
            setClause.append(field).append(" = ?");
        }

        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Remplir les valeurs des paramètres SQL
            int index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);
            stmt.executeUpdate();
        }
    }

    // ... autres méthodes et code existant ...
}