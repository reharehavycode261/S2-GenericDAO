package database.core;

import java.sql.*;

public class GenericDAO<T> {

    private DBConnection connection;
    private String tableName;

    public GenericDAO(DBConnection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // Existence d'autres méthodes dans la classe...

    /**
     * Compte le nombre total d'enregistrements dans la table
     * @return Le nombre d'enregistrements
     * @throws SQLException en cas d'erreur SQL
     */
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }
    
    // ... reste du code existant ...
}