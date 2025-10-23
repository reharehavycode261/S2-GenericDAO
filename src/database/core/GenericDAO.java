package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    // ... code existant ...

    /**
     * Récupère une liste paginée des enregistrements à partir de la base de données.
     * @param offset L'indice du premier enregistrement à récupérer.
     * @param limit Le nombre maximum d'enregistrements à récupérer.
     * @return Une liste d'enregistrements pour la page spécifiée.
     * @throws SQLException en cas d'erreur SQL.
     */
    public List<T> paginate(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    T entity = mapRow(rs);
                    results.add(entity);
                }
                return results;
            }
        }
    }

    /**
     * Méthode à implémenter pour mapper une ligne de ResultSet à une entité.
     * @param rs Le ResultSet contenant les données de la ligne actuelle.
     * @return L'entité mappée.
     * @throws SQLException en cas d'erreur SQL.
     */
    protected T mapRow(ResultSet rs) throws SQLException {
        // Implémenter le mappage des résultats vers l'entité désirée
        return null; // à implémenter selon l'entité
    }
    
    // ... reste du code existant ...
}