package database.core;

import java.sql.*;
import java.util.List;

public class GenericDAO<T> {
    // Hypothèse: Connection et tableName sont des attributs déjà définis dans la classe
    private Connection connection;
    private String tableName;

    /**
     * Met à jour des champs spécifiques dans la base de données pour un enregistrement correspondant à un critère donné.
     * @param updates Une liste d'Affectation représentant les colonnes et les nouvelles valeurs à mettre à jour.
     * @param whereClause La clause WHERE pour spécifier quel(s) enregistrement(s) mettre à jour.
     * @throws SQLException En cas d'erreur SQL.
     */
    public void updateFields(List<Affectation> updates, String whereClause) throws SQLException {
        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("La liste des mises à jour ne peut être vide.");
        }

        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < updates.size(); i++) {
            Affectation affectation = updates.get(i);
            sql.append(affectation.getColumn()).append(" = ?");
            if (i < updates.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ").append(whereClause);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < updates.size(); i++) {
                stmt.setObject(i + 1, updates.get(i).getValue());
            }
            stmt.executeUpdate();
        }
    }
}