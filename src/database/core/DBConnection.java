package database.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DBConnection {
    Database database;
    Connection connection;

    public DBConnection(Database database, Connection connection) {
        setDatabase(database);
        setConnection(connection);
    }

    public void commit() throws SQLException {
        getConnection().commit();
    }

    public void rollback() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour certains champs spécifiques d'un enregistrement dans la base de données.
     *
     * @param tableName Le nom de la table
     * @param fields    Les champs à mettre à jour
     * @param values    Les nouvelles valeurs des champs
     * @param condition La condition pour sélectionner l'enregistrement à mettre à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(String tableName, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("La longueur des champs et des valeurs doit correspondre.");
        }

        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");

        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(" WHERE ").append(condition);

        try (PreparedStatement stmt = getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }

            stmt.executeUpdate();
        }
    }

    private Connection getConnection() {
        return this.connection;
    }

    private void setDatabase(Database database) {
        this.database = database;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }
}