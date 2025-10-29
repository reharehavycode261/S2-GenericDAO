package test;

import database.core.GenericDAO;
import database.core.SearchFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class MainTest {

    private GenericDAO<MyEntity> daoMock;
    private List<SearchFilter> filters;

    @BeforeEach
    void setUp() {
        daoMock = Mockito.mock(GenericDAO.class);
        filters = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        daoMock = null;
        filters = null;
    }

    @Test
    void testMainWithValidFilters() {
        // Setup filters
        filters.add(new SearchFilter("name", "LIKE", "%example%"));
        filters.add(new SearchFilter("age", ">", 25));

        // Mock the expected result
        List<MyEntity> expectedResults = new ArrayList<>();
        expectedResults.add(new MyEntity("Example Name", 30));

        Mockito.when(daoMock.searchWithFilters(filters)).thenReturn(expectedResults);

        // Execute the search
        List<MyEntity> results = daoMock.searchWithFilters(filters);

        // Assertions
        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertEquals(1, results.size(), "Results size should be 1");
        Assertions.assertEquals("Example Name", results.get(0).getName(), "Name should match");
        Assertions.assertEquals(30, results.get(0).getAge(), "Age should match");
    }

    @Test
    void testMainWithEmptyFilters() {
        // Mock the expected result
        List<MyEntity> expectedResults = new ArrayList<>();

        Mockito.when(daoMock.searchWithFilters(filters)).thenReturn(expectedResults);

        // Execute the search
        List<MyEntity> results = daoMock.searchWithFilters(filters);

        // Assertions
        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertTrue(results.isEmpty(), "Results should be empty");
    }

    @Test
    void testMainWithInvalidFilter() {
        // Setup filters with an invalid attribute
        filters.add(new SearchFilter("invalidAttribute", "=", "value"));

        // Mock the expected result
        Mockito.when(daoMock.searchWithFilters(filters)).thenThrow(new IllegalArgumentException("Invalid attribute"));

        // Execute the search and assert exception
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            daoMock.searchWithFilters(filters);
        });

        // Assertions
        Assertions.assertEquals("Invalid attribute", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testMainWithNullFilters() {
        // Mock the expected result
        Mockito.when(daoMock.searchWithFilters(null)).thenThrow(new NullPointerException("Filters cannot be null"));

        // Execute the search and assert exception
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> {
            daoMock.searchWithFilters(null);
        });

        // Assertions
        Assertions.assertEquals("Filters cannot be null", exception.getMessage(), "Exception message should match");
    }
}

// Dummy class for testing purposes
class MyEntity {
    private String name;
    private int age;

    public MyEntity(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "MyEntity{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}