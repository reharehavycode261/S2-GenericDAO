package test;

import database.core.GenericDAO;
import database.core.SearchFilter;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            GenericDAO<MyEntity> dao = new GenericDAO<>();
            List<SearchFilter> filters = new ArrayList<>();

            filters.add(new SearchFilter("name", "LIKE", "%example%"));
            filters.add(new SearchFilter("age", ">", 25));

            List<MyEntity> results = dao.searchWithFilters(filters);
            for (MyEntity result : results) {
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Dummy class for testing purposes
class MyEntity {
    private String name;
    private int age;
    
    // Constructors, getters and setters...

    @Override
    public String toString() {
        return "MyEntity{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}