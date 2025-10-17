package database.core;

import java.sql.*;

/**
 * Classe générique DAO pour les opérations de base de données.
 */
public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // ... autres méthodes ...

    /**
     * Compte le nombre total d'enregistrements dans la table.
     * 
     * @param condition La condition optionnelle pour filtrer les enregistrements.
     * @return Le nombre d'enregistrements.
     * @throws SQLException en cas d'erreur SQL.
     */
    public long count(String condition) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        if (condition != null && !condition.isEmpty()) {
            query += " WHERE " + condition;
        }

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }

    public long count() throws SQLException {
        return count(null);
    }

    // ... reste du code existant ...
}