package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO {
    String id;
    Connection connection;

    // Constructeur
    public GenericDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Met à jour certains champs d'un enregistrement dans la table
     * 
     * @param tableName Le nom de la table
     * @param fieldsValues Une map contenant les noms des champs et leurs nouvelles valeurs
     * @param whereClause La clause WHERE pour cibler l'enregistrement spécifique à mettre à jour
     * @throws SQLException En cas d'erreur SQL
     */
    public void updateFields(String tableName, Map<String, Object> fieldsValues, String whereClause) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");
        
        int i = 0;
        for (String field : fieldsValues.keySet()) {
            sql.append(field).append(" = ?");
            if (i < fieldsValues.size() - 1) {
                sql.append(", ");
            }
            i++;
        }

        sql.append(" WHERE ").append(whereClause);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            i = 1;
            for (Object value : fieldsValues.values()) {
                stmt.setObject(i++, value);
            }
            stmt.executeUpdate();
        }
    }
}