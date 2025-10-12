package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;

public class GenericDAO {
    String id;
    Connection connection;
    String tableName;

    // Constructeur et autres méthodes existantes...

    /**
     * Compte le nombre total d'enregistrements dans la table
     * @return Le nombre d'enregistrements
     * @throws SQLException en cas d'erreur SQL
     */
    public long count() throws SQLException {
        return count(null);
    }

    /**
     * Compte le nombre d'enregistrements en fonction de la condition spécifiée
     * @param condition La condition WHERE optionnelle pour le comptage
     * @return Le nombre d'enregistrements qui satisfont la condition
     * @throws SQLException en cas d'erreur SQL
     */
    public long count(String condition) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        if (condition != null && !condition.trim().isEmpty()) {
            sql += " WHERE " + condition;
        }
        
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