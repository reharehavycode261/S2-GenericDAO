package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {

    protected Connection connection;
    protected String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // ... autres méthodes ...

    /**
     * Met à jour les champs spécifiques d'une entité dans la base de données.
     * @param entity l'entité à mettre à jour
     * @param fieldsToUpdate map contenant les champs et leurs nouvelles valeurs
     * @return nombre de lignes affectées
     * @throws SQLException en cas d'erreur SQL
     */
    public int updateFields(T entity, Map<String, Object> fieldsToUpdate) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("fieldsToUpdate cannot be null or empty");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (String field : fieldsToUpdate.keySet()) {
            sql.append(field).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2); // Remove last comma
        // Assuming the entity has a method getId() to get the primary key
        sql.append(" WHERE id = ?");

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fieldsToUpdate.values()) {
                statement.setObject(index++, value);
            }
            // Assuming the entity has a method getId() to get the primary key
            statement.setObject(index, getIdFromEntity(entity));
            return statement.executeUpdate();
        }
    }

    private Object getIdFromEntity(T entity) {
        // Logic to extract ID from entity
        // This is a mock-up code which needs to be replaced with actual implementation based on how an entity is structured.
        return 0; // Placeholder for a real ID extraction
    }

    // ... autres méthodes ...
}