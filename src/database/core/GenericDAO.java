package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringJoiner;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // ... autres méthodes existantes ...

    /**
     * Mettre à jour des champs spécifiques d'un enregistrement dans la base de données.
     *
     * @param dbConnection La connexion à la base de données
     * @param fields       Les noms des champs à mettre à jour
     * @param values       Les valeurs correspondantes pour mettre à jour les champs
     * @param condition    La condition SQL pour identifier l'enregistrement à mettre à jour
     * @throws SQLException En cas d'erreur SQL
     */
    public void updateFields(DBConnection dbConnection, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields == null || values == null || fields.length == 0 || fields.length != values.length) {
            throw new IllegalArgumentException("Les champs et les valeurs doivent être non nuls et de la même longueur");
        }
        
        StringJoiner setClause = new StringJoiner(", ");
        for (String field : fields) {
            setClause.add(field + " = ?");
        }
        
        String sql = "UPDATE " + tableName + " SET " + setClause.toString() + " WHERE " + condition;
        
        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }
            stmt.executeUpdate();
        }
    }
    
    // ... autres méthodes existantes ...
}