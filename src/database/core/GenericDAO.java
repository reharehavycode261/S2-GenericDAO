package database.core;

import java.sql.*;

public class GenericDAO {
    String id;
    private DBConnection connection;
    private String tableName;

    public GenericDAO() {
        // Constructeur par défaut
    }

    public void createTable(DBConnection dbConnection) throws SQLException {
        // Exemple de méthode existante
    }

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
}