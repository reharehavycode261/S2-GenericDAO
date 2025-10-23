package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // Méthode existante pour obtenir tous les enregistrements
    public List<T> findAll() throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return resultSetToList(rs);
        }
    }

    /**
     * Méthode pour obtenir une page d'enregistrements.
     * 
     * @param offset Le décalage de départ pour la pagination.
     * @param limit Le nombre maximum d'enregistrements à récupérer.
     * @return Une liste d'enregistrements limités par l'offset et le limit.
     * @throws SQLException en cas d'erreur SQL.
     */
    public List<T> findPage(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                return resultSetToList(rs);
            }
        }
    }

    // Méthode auxiliaire pour convertir un ResultSet en liste d'objets T
    private List<T> resultSetToList(ResultSet rs) throws SQLException {
        List<T> list = new ArrayList<>();
        // Logic to convert ResultSet rows into objects of type T
        // For demonstration purposes, this code is simplified and assumes a method to convert rows exists
        while (rs.next()) {
            T obj = convertRowToObject(rs);
            list.add(obj);
        }
        return list;
    }

    private T convertRowToObject(ResultSet rs) throws SQLException {
        // Conversion logic should be implemented based on specific requirements
        // This is just a placeholder for demonstration
        return null;
    }
}