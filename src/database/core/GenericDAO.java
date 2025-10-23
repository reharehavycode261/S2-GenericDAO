package database.core;

import java.sql.*;
import java.util.List;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // ... Autres méthodes existantes ...

    /**
     * Exécute une liste de commandes SQL en batch dans une seule transaction.
     * @param sqlCommands La liste des commandes SQL à exécuter
     * @throws SQLException si une erreur survient lors de l'exécution des commandes
     */
    public void executeBatch(List<String> sqlCommands) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            connection.setAutoCommit(false);
            try {
                for (String sql : sqlCommands) {
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}