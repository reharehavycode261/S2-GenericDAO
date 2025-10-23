package test;

import database.core.GenericDAO;
import database.core.Affectation;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GenericDAOTest {
    private static Connection connection;
    private TestDAO dao;

    @BeforeAll
    static void setUpClass() throws Exception {
        // Establish the connection for tests
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        stmt.execute("INSERT INTO test_table (id, name, age) VALUES (1, 'John Doe', 30)");
        stmt.execute("INSERT INTO test_table (id, name, age) VALUES (2, 'Jane Doe', 25)");
    }

    @BeforeEach
    void setUp() {
        dao = new TestDAO(connection, "test_table");
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        connection.close();
    }

    @Test
    void updateFieldsTest() throws SQLException {
        List<Affectation> updates = Arrays.asList(
            new Affectation("name", "John Smith"),
            new Affectation("age", 31)
        );

        dao.updateFields(1, updates);

        // Verify updates
        String sql = "SELECT name, age FROM test_table WHERE id = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            assertTrue(rs.next());
            assertEquals("John Smith", rs.getString("name"));
            assertEquals(31, rs.getInt("age"));
        }
    }

    @Test
    void findAllWithPaginationTest() throws SQLException {
        // Retrieve first page with one record per page
        List<TestEntity> result = dao.findAllWithPagination(1, 1);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());

        // Retrieve second page with one record per page
        result = dao.findAllWithPagination(2, 1);
        assertEquals(1, result.size());
        assertEquals("Jane Doe", result.get(0).getName());
    }
}

// Dummy implementation of GenericDAO for testing purposes
class TestDAO extends GenericDAO<TestEntity> {
    public TestDAO(Connection connection, String tableName) {
        super(connection, tableName);
    }

    @Override
    protected TestEntity resultSetToEntity(ResultSet rs) throws SQLException {
        TestEntity entity = new TestEntity();
        entity.setId(rs.getInt("id"));
        entity.setName(rs.getString("name"));
        entity.setAge(rs.getInt("age"));
        return entity;
    }
}

// Dummy entity for testing
class TestEntity {
    private int id;
    private String name;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}