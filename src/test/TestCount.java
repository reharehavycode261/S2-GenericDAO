package test;

import database.core.Database;
import database.core.GenericDAO;
import database.exception.SQL.DatabaseSQLException;
import database.provider.PostgreSQL;

public class TestCount {
    public static void main(String[] args) {
        try {
            // Initialize database connection
            Database db = new PostgreSQL("localhost", "5432", "test_db", "postgres", "password");
            
            // Test with Student table
            Student student = new Student();
            student.setDatabase(db);
            student.setTableName("student");
            
            // Test basic count
            long totalStudents = student.count();
            System.out.println("Total number of students: " + totalStudents);
            
            // Test count with condition
            long studentsWithCondition = student.count("age > 20");
            System.out.println("Number of students over 20 years old: " + studentsWithCondition);
            
            // Test with empty table
            EmptyTest emptyTest = new EmptyTest();
            emptyTest.setDatabase(db);
            emptyTest.setTableName("empty_test");
            long emptyCount = emptyTest.count();
            System.out.println("Count in empty table: " + emptyCount);
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class EmptyTest extends GenericDAO {
    // Empty class for testing with empty table
}