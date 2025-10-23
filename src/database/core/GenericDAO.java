package database.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDAO<T> {
    String id;
    Connection connection;
    String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Récupère une page de résultats
     * @param limit Le nombre d'enregistrements par page
     * @param offset Le décalage à partir duquel récupérer les enregistrements
     * @return La liste des enregistrements pour la page spécifiée
     * @throws SQLException si une erreur SQL se produit
     */
    public List<T> getPaginatedResults(int limit, int offset) throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Conversion de l'enregistrement en objet de type T (simplifié ici)
                    T entity = (T) rs.getObject(1);
                    results.add(entity);
                }
            }
        }
        return results;
    }

    // ... autres méthodes ...
}