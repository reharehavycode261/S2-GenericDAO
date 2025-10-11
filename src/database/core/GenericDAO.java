package database.core;

import database.exception.SQL.DatabaseSQLException;
import database.exception.SQL.TableNotFoundException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericDAO {
    private String tableName;
    private Database database;

    public GenericDAO() {
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    /**
     * Count the number of records in the table
     * @return The number of records
     * @throws DatabaseSQLException if a SQL error occurs
     * @throws TableNotFoundException if the table does not exist
     */
    public long count() throws DatabaseSQLException, TableNotFoundException {
        if (this.tableName == null || this.tableName.trim().isEmpty()) {
            throw new TableNotFoundException("Table name is not set");
        }
        
        try {
            String query = "SELECT COUNT(*) FROM " + this.tableName;
            ResultSet rs = this.database.executeQuery(query);
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseSQLException("Error counting records in table " + this.tableName + ": " + e.getMessage());
        }
    }

    /**
     * Count the number of records in the table that match the given condition
     * @param condition The WHERE clause condition
     * @return The number of matching records
     * @throws DatabaseSQLException if a SQL error occurs
     * @throws TableNotFoundException if the table does not exist
     */
    public long count(String condition) throws DatabaseSQLException, TableNotFoundException {
        if (this.tableName == null || this.tableName.trim().isEmpty()) {
            throw new TableNotFoundException("Table name is not set");
        }
        
        try {
            String query = "SELECT COUNT(*) FROM " + this.tableName;
            if (condition != null && !condition.trim().isEmpty()) {
                query += " WHERE " + condition;
            }
            
            ResultSet rs = this.database.executeQuery(query);
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseSQLException("Error counting records in table " + this.tableName + " with condition: " + e.getMessage());
        }
    }

    // [Existing methods remain unchanged...]
}