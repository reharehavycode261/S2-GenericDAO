package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

class DBConnectionTest {

    private DBConnection dbConnection;
    private Config mockConfig;

    @BeforeEach
    void setUp() {
        dbConnection = new DBConnection();
        mockConfig = Mockito.mock(Config.class);
    }

    @AfterEach
    void tearDown() {
        dbConnection = null;
        mockConfig = null;
    }

    @Test
    void testConnectSuccess() throws SQLException {
        // Arrange
        String url = "jdbc:mysql://localhost:3306/testdb";
        String username = "testuser";
        String password = "testpass";
        when(mockConfig.getUrl()).thenReturn(url);
        when(mockConfig.getUsername()).thenReturn(username);
        when(mockConfig.getPassword()).thenReturn(password);

        // Act
        Connection connection = dbConnection.connect(mockConfig);

        // Assert
        Assertions.assertNotNull(connection, "Connection should not be null on successful connection");
        Assertions.assertFalse(connection.isClosed(), "Connection should be open after successful connection");
    }

    @Test
    void testConnectInvalidUrl() {
        // Arrange
        when(mockConfig.getUrl()).thenReturn("invalid_url");
        when(mockConfig.getUsername()).thenReturn("testuser");
        when(mockConfig.getPassword()).thenReturn("testpass");

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            dbConnection.connect(mockConfig);
        }, "SQLException should be thrown for invalid URL");
    }

    @Test
    void testConnectInvalidCredentials() {
        // Arrange
        String url = "jdbc:mysql://localhost:3306/testdb";
        when(mockConfig.getUrl()).thenReturn(url);
        when(mockConfig.getUsername()).thenReturn("wronguser");
        when(mockConfig.getPassword()).thenReturn("wrongpass");

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            dbConnection.connect(mockConfig);
        }, "SQLException should be thrown for invalid credentials");
    }

    @Test
    void testConnectNullConfig() {
        // Act & Assert
        Assertions.assertThrows(NullPointerException.class, () -> {
            dbConnection.connect(null);
        }, "NullPointerException should be thrown when config is null");
    }
}