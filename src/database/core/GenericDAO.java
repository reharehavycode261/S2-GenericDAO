package database.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Met à jour les champs spécifiés de l'enregistrement correspondant dans la table.
     * 
     * @param entity   L'objet contenant les nouveaux champs et leurs valeurs
     * @param condition La condition pour sélectionner les enregistrements à mettre à jour
     * @return true si les enregistrements ont été mis à jour, false sinon
     * @throws SQLException en cas d'erreur SQL
     */
    public boolean updateFields(T entity, String condition) throws SQLException {
        // Construction de la requête SQL de mise à jour
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");

        // Récupération des champs de l'entité
        Field[] fields = entity.getClass().getDeclaredFields();
        boolean first = true;

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(entity);
            if (value != null) {
                if (!first) {
                    sql.append(", ");
                }
                sql.append(field.getName()).append(" = ?");
                first = false;
            }
        }

        sql.append(" WHERE ").append(condition);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value != null) {
                    stmt.setObject(index++, value);
                }
            }
            return stmt.executeUpdate() > 0;
        } catch (IllegalAccessException e) {
            throw new SQLException("Field access error", e);
        }
    }
}