package database.core;

import java.sql.*;

/**
 * Classe générique pour l'accès aux données.
 * @param <T> Le type d'objet géré par le DAO.
 */
public class GenericDAO<T> {
    private String tableName;
    private Connection connection;

    public GenericDAO(String tableName, Connection connection) {
        this.tableName = tableName;
        this.connection = connection;
    }

    /**
     * Compte le nombre total d'enregistrements dans la table.
     *
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
        }
        return 0;
    }

    // ... autres méthodes existantes ...
}