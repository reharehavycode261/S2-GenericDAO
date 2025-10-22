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
     * Met à jour certains champs spécifiques d'un objet dans la base de données.
     * 
     * @param tableName Le nom de la table à mettre à jour
     * @param fields Les champs à mettre à jour
     * @param values Les nouvelles valeurs des champs
     * @param condition La condition pour spécifier les enregistrements à mettre à jour
     * @throws SQLException Si une erreur SQL survient
     */
    public void updateFields(String tableName, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs doit correspondre au nombre de valeurs.");
        }

        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");

        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(" WHERE ").append(condition);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
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