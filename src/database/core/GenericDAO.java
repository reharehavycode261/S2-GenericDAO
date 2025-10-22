package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericDAO<T> {
    private String tableName; // Assuming this is defined elsewhere
    private Connection connection; // Assuming this is defined elsewhere

    // Existing methods...

    /**
     * Met à jour certains champs spécifiques d'un objet dans la base de données.
     * @param dbConnection Connexion à la base de données
     * @param fields Noms des champs à mettre à jour
     * @param values Nouvelles valeurs pour les champs
     * @param condition Condition pour identifier les enregistrements (ex: "id = ?")
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(Connection dbConnection, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs doit être égal au nombre de valeurs");
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < fields.length; i++) {
            sqlBuilder.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(" WHERE ").append(condition);

        try (PreparedStatement pstmt = dbConnection.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
        }
    }

    // Other methods...
}