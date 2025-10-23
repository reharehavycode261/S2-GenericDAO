package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;
    
    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Paginer les résultats de la base de données
     * @param page Le numéro de la page à récupérer
     * @param pageSize Le nombre d'éléments par page
     * @return Liste des éléments paginés
     * @throws SQLException en cas d'erreur SQL
     */
    public List<T> findAllPaginated(int page, int pageSize) throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    T entity = mapResultSetToEntity(rs);
                    results.add(entity);
                }
            }
        }
        return results;
    }

    /**
     * Mapper un ResultSet à l'entité générique
     * Doit être implémenté dans les sous-classes spécifiques pour traiter le mappage
     * @param rs Le ResultSet de la requête
     * @return L'entité de type T
     * @throws SQLException en cas d'erreur SQL
     */
    protected T mapResultSetToEntity(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Doit être implémenté dans les sous-classes spécifiques");
    }

    // ... reste du code existant ...
}