package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GenericDAO {
    private Database database;
    private String tableName;

    public GenericDAO() {
        this.tableName = DBTool.getTableName(this.getClass());
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * Counts the total number of records in the table
     * @return the total count of records
     * @throws SQLException if a database error occurs
     */
    public long count() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        long count = 0;

        try {
            connection = database.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT COUNT(*) FROM " + this.getTableName();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return count;
    }

    // Autres méthodes existantes...
}