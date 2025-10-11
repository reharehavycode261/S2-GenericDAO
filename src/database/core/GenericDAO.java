package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T> {
    protected Database database;
    protected String tableName;
    
    public GenericDAO(Database database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }
    
    protected abstract T createEntity(ResultSet rs) throws SQLException;
    
    public List<T> findAll() throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                results.add(createEntity(rs));
            }
        }
        
        return results;
    }

    /**
     * Compte le nombre total d'enregistrements dans la table.
     * 
     * @return le nombre total d'enregistrements
     * @throws SQLException si une erreur SQL survient
     */
    public long count() throws SQLException {
        return count(null);
    }

    /**
     * Compte le nombre d'enregistrements dans la table qui correspondent à une condition WHERE.
     * 
     * @param whereClause la clause WHERE SQL (sans le mot-clé "WHERE"), peut être null
     * @return le nombre d'enregistrements correspondant aux critères
     * @throws SQLException si une erreur SQL survient
     */
    public long count(String whereClause) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM ").append(tableName);
        
        if (whereClause != null && !whereClause.trim().isEmpty()) {
            query.append(" WHERE ").append(whereClause);
        }
        
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString());
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }
}