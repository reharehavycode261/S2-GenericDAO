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
        List<T> entities = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                entities.add(createEntity(rs));
            }
        }
        return entities;
    }

    public long count() throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }
}