package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    // Constructeur et autres méthodes...

    /**
     * Compte le nombre total d'enregistrements dans la table
     * @param condition Condition SQL supplémentaire pour filtrer les résultats
     * @return Le nombre d'enregistrements
     * @throws SQLException en cas d'erreur SQL
     */
    public long count(String condition) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + tableName);
        if (condition != null && !condition.trim().isEmpty()) {
            sql.append(" WHERE ").append(condition);
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString());
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }
}