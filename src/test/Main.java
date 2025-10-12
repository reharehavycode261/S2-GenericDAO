package test;

import database.core.GenericDAO;

public class Main {

    public static void main(String[] args) {
        GenericDAO dao = new GenericDAO();
        
        // Test count method
        String tableName = "test_table"; // Replace with an actual table name
        int numberOfRecords = dao.count(tableName);
        
        System.out.println("Number of records in " + tableName + ": " + numberOfRecords);
    }
}