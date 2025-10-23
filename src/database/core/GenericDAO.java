package database.core;

import java.sql.*;
import java.util.List;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;
    private String idColumn;

    // Constructeur existant
    public GenericDAO(Connection connection, String tableName, String idColumn) {
        this.connection = connection;
        this.tableName = tableName;
        this.idColumn = idColumn;
    }

    // ... Autres méthodes existantes ...

    /**
     * Met à jour les champs spécifiques d'un enregistrement.
     *
     * @param id Identifiant de l'enregistrement à mettre à jour
     * @param updates Liste des colonnes et nouvelles valeurs
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(Object id, List<Affectation> updates) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        int updateCount = 0;

        for (Affectation affectation : updates) {
            if (updateCount++ > 0) {
                sql.append(", ");
            }
            sql.append(affectation.getColumn()).append(" = ?");
        }
        sql.append(" WHERE ").append(idColumn).append(" = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Affectation affectation : updates) {
                stmt.setObject(index++, affectation.getValue());
            }
            stmt.setObject(index, id);

            stmt.executeUpdate();
        }
    }

    // ... Reste du code existant ...
}