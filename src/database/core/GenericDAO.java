package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GenericDAO extends Database {
    String id;

    public void createTable(DBConnection dbConnection) throws SQLException, AttributeMissingException {
        // existing implementation
    }

    public List<?> executeQuery(String query) throws SQLException {
        List<?> result = getFromCache(query);
        if (result == null) {
            try (Statement stmt = dbConnection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    result = processResultSet(rs);
                    addToCache(query, result);
                }
            }
        }
        return result;
    }

    private List<?> processResultSet(ResultSet rs) throws SQLException {
        // Convert ResultSet to list of objects
        return List.of(); // Placeholder
    }
    
    public void insertOrUpdateRecord(String query) throws SQLException {
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.executeUpdate(query);
            clearCache(); // Invalidate cache on data change
        }
    }

    // Rappel: Utiliser clearCache() dans les méthodes de modification de données.
}