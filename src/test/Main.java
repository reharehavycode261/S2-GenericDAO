package test;

import database.core.Database;
import database.core.GenericDAO;
import database.provider.PostgreSQL;

public class Main {
    public static void main(String[] args) {
        try {
            // Test de la méthode count()
            Database database = new PostgreSQL("jdbc:postgresql://localhost:5432/test", "postgres", "postgres");
            
            // Test avec la table Student
            GenericDAO<Student> studentDAO = new GenericDAO<>(database, "student", Student.class);
            long studentCount = studentDAO.count();
            System.out.println("Nombre total d'étudiants: " + studentCount);
            
            // Test avec condition WHERE
            long studentCountWithCondition = studentDAO.count("age > ?", 20);
            System.out.println("Nombre d'étudiants de plus de 20 ans: " + studentCountWithCondition);
            
            // Test avec la table Emp
            GenericDAO<Emp> empDAO = new GenericDAO<>(database, "emp", Emp.class);
            long empCount = empDAO.count();
            System.out.println("Nombre total d'employés: " + empCount);
            
            // Test avec condition WHERE
            long empCountWithCondition = empDAO.count("salary > ?", 50000);
            System.out.println("Nombre d'employés avec un salaire > 50000: " + empCountWithCondition);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}