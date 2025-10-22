package database.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringJoiner;

public class GenericDAO<T> {
    private DBConnection dbConnection;
    private String tableName;

    public GenericDAO(DBConnection dbConnection, String tableName) {
        this.dbConnection = dbConnection;
        this.tableName = tableName;
    }

    /**
     * Met à jour des champs spécifiques d'un enregistrement dans la base de données.
     * @param fields Les champs à mettre à jour
     * @param values Les nouvelles valeurs pour ces champs
     * @param condition La condition WHERE pour sélectionner les enregistrements à mettre à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length == 0 || fields.length != values.length) {
            throw new IllegalArgumentException("Fields and values must have the same non-zero length.");
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