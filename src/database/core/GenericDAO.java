package database.core;

import java.sql.*;

/**
 * Classe générique pour les opérations DAO
 */
public class GenericDAO<T> {
    private final Connection connection;
    private final String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // ... autres méthodes existantes ...

    /**
     * Compte le nombre total d'enregistrements dans la table
     * @return Le nombre d'enregistrements
     * @throws SQLException en cas d'erreur SQL
     */
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }

    // ... reste du code existant ...
}