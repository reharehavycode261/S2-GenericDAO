package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    // ... code existant ...

    /**
     * Met à jour les champs spécifiés pour un enregistrement identifié par sa clé primaire.
     * @param id L'identifiant unique de l'enregistrement à mettre à jour.
     * @param updates La liste des affectations représentant les colonnes et leurs nouvelles valeurs.
     * @throws SQLException en cas d'erreur SQL.
     */
    public void updateFields(Object id, List<Affectation> updates) throws SQLException {
        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("Les mises à jour ne peuvent pas être nulles ou vides.");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        List<Object> values = new ArrayList<>();

        for (int i = 0; i < updates.size(); i++) {
            Affectation affectation = updates.get(i);
            sql.append(affectation.getColumn()).append(" = ?");
            values.add(affectation.getValue());
            if (i < updates.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE id = ?");
        values.add(id);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.executeUpdate();
        }
    }
    
    // ... reste du code existant ...
}