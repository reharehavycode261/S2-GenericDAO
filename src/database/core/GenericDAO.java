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
     * Met à jour les champs d'un enregistrement spécifié par l'identifiant.
     * @param id L'identifiant de l'enregistrement à mettre à jour.
     * @param updates La liste des champs à mettre à jour et leurs nouvelles valeurs.
     * @throws SQLException en cas d'erreur SQL.
     */
    public void updateFields(int id, List<Affectation> updates) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < updates.size(); i++) {
            sql.append(updates.get(i).getColumn()).append(" = ?");
            if (i < updates.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < updates.size(); i++) {
                stmt.setObject(i + 1, updates.get(i).getValue());
            }
            stmt.setInt(updates.size() + 1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Récupère une liste d'enregistrements en utilisant la pagination.
     * @param pageNum Le numéro de la page à récupérer.
     * @param pageSize Le nombre d'enregistrements par page.
     * @return Une liste d'enregistrements.
     * @throws SQLException en cas d'erreur SQL.
     */
    public List<T> findAllWithPagination(int pageNum, int pageSize) throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageSize * (pageNum - 1));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Assumes a method to convert a ResultSet row to a T object.
                    T row = resultSetToEntity(rs);
                    results.add(row);
                }
            }
        }
        return results;
    }

    // Method assumed to exist to convert ResultSet to T entity
    protected abstract T resultSetToEntity(ResultSet resultSet) throws SQLException;
}