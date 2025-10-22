package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
     * Met à jour des champs spécifiques d'un enregistrement dans la base de données.
     * 
     * @param tableName Le nom de la table à mettre à jour
     * @param fields Les champs à mettre à jour
     * @param values Les nouvelles valeurs pour ces champs
     * @param condition La condition WHERE pour identifier l'enregistrement
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(String tableName, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs et de valeurs doit être le même.");
        }

        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < fields.length; i++) {
            query.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                query.append(", ");
            }
        }
        query.append(" WHERE ").append(condition);

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }
            stmt.executeUpdate();
        }
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}