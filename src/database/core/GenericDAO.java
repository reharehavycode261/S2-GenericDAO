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
    
    // Méthode de pagination
    public List<T> findAllPaginated(int pageNumber, int pageSize) throws SQLException {
        List<T> resultList = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Supposons qu'une méthode de conversion de ResultSet à l'objet T existe
                    T obj = resultSetToObject(rs);
                    resultList.add(obj);
                }
            }
        }
        
        return resultList;
    }

    // Hypothetical method to convert a ResultSet to an object of type T
    private T resultSetToObject(ResultSet rs) {
        // This is a placeholder for the actual implementation
        // depending on the specifics of how T should be constructed from rs
        return null;
    }
}