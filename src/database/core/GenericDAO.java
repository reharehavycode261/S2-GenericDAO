package database.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GenericDAO<T> {

    private String tableName;
    private Connection connection;

    public GenericDAO(String tableName, Connection connection) {
        this.tableName = tableName;
        this.connection = connection;
    }
    
    /**
     * Met à jour les champs spécifiés de l'objet dans la base de données.
     * 
     * @param entity Objet contenant les nouvelles valeurs des champs.
     * @param fields Liste des noms des champs à mettre à jour.
     * @throws SQLException Si une erreur SQL se produit.
     */
    public void updateFields(T entity, List<String> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("La liste des champs à mettre à jour ne peut pas être vide.");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (String field : fields) {
            sql.append(field).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2); // Supprimez la dernière virgule
        sql.append(" WHERE id = ?"); // Supposons que l'entité a un champ "id"

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (String field : fields) {
                Field classField = entity.getClass().getDeclaredField(field);
                classField.setAccessible(true);
                statement.setObject(index++, classField.get(entity));
            }
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            statement.setObject(index, idField.get(entity));

            statement.executeUpdate();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new SQLException("Erreur lors de la tentative d'accès aux champs de l'objet.", e);
        }
    }
}