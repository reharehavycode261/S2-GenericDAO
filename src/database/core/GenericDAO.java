package database.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;
    protected Class<T> type;

    public GenericDAO(Connection connection, String tableName, Class<T> type) {
        this.connection = connection;
        this.tableName = tableName;
        this.type = type;
    }

    // Méthode existante

    /**
     * Récupère une liste d'enregistrements avec pagination
     * @param pageNumber Le numéro de la page à récupérer
     * @param pageSize Le nombre d'éléments par page
     * @return Une liste d'enregistrements
     * @throws SQLException en cas d'erreur SQL
     */
    public List<T> findAllWithPagination(int pageNumber, int pageSize) throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (pageNumber - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    T entity = type.getDeclaredConstructor().newInstance();
                    // Supposons que nous avons une méthode pour remplir une entité à partir d'un ResultSet
                    populateEntity(rs, entity);
                    results.add(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SQLException("Erreur lors de la création des instances", e);
            }
        }
        return results;
    }

    /**
     * Remplit l'entité à partir d'un ResultSet
     * @param rs Le ResultSet avec les données
     * @param entity L'entité à remplir
     * @throws SQLException en cas d'erreur SQL
     */
    private void populateEntity(ResultSet rs, T entity) throws SQLException {
        // Logique pour remplir les propriétés de l'entité T à partir du ResultSet
    }

    // Autres méthodes existantes
}