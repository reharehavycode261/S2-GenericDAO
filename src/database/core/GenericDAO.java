package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class GenericDAO<T> {
    String id;
    private Connection connection;
    private String tableName;
    
    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }
    
    // Autres méthodes existantes...
    
    /**
     * Récupère une page de résultats à partir de la table
     * @param limit Le nombre maximum de résultats à récupérer
     * @param offset Le nombre de résultats à ignorer avant de commencer la récupération
     * @return Une liste de résultats de la taille spécifiée
     * @throws SQLException en cas d'erreur SQL
     */
    public List<T> getPage(int limit, int offset) throws SQLException, IllegalAccessException, InstantiationException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();
                
                while (rs.next()) {
                    T instance = (T)Class.forName(tableName).newInstance(); // Assumes tableName matches class name
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metadata.getColumnName(i);
                        Object value = rs.getObject(i);
                        
                        // Assumes that setters are following the pattern setX(...)
                        String setterName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                        instance.getClass().getMethod(setterName, value.getClass()).invoke(instance, value);
                    }
                    results.add(instance);
                }
            }
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
            throw new SQLException("Error processing result set", e);
        }
        return results;
    }
}