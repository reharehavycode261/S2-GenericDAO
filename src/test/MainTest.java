package test;

import database.core.GenericDAO;

import org.junit.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainTest {

    private static Connection connection;
    private GenericDAO<Object> dao;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        // Configurer une connexion de test à la base de données
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
    }

    @Before
    public void setUp() {
        dao = new GenericDAO<>(connection, "TestTable");
    }

    @Test
    public void testExecuteBatch() throws SQLException {
        List<String> sqlCommands = new ArrayList<>();
        sqlCommands.add("CREATE TABLE IF NOT EXISTS TestTable (ID INT PRIMARY KEY, Name VARCHAR(255))");
        sqlCommands.add("INSERT INTO TestTable (ID, Name) VALUES (1, 'John')");
        sqlCommands.add("INSERT INTO TestTable (ID, Name) VALUES (2, 'Jane')");
        sqlCommands.add("UPDATE TestTable SET Name='Doe' WHERE ID=1");
        sqlCommands.add("DELETE FROM TestTable WHERE ID=2");

        dao.executeBatch(sqlCommands);

        // Vérifier les résultats attendus
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM TestTable")) {

            Assert.assertTrue(rs.next());
            Assert.assertEquals(1, rs.getInt("ID"));
            Assert.assertEquals("Doe", rs.getString("Name"));

            Assert.assertFalse(rs.next());
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}