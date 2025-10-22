package test;

import database.core.Affectation;
import database.core.GenericDAO;
import database.core.DBConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

class MainTest {

    private Connection mockConnection;
    private GenericDAO mockDAO;
    private DBConnection mockDBConnection;

    @BeforeEach
    void setUp() {
        // Setup mock objects
        mockConnection = Mockito.mock(Connection.class);
        mockDAO = Mockito.mock(GenericDAO.class);
        mockDBConnection = Mockito.mock(DBConnection.class);
    }

    @AfterEach
    void tearDown() {
        // Clean up resources if needed
    }

    @Test
    void testMainMethodSuccess() {
        // Arrange
        Mockito.doNothing().when(mockDAO).updateFields(Mockito.anyString(), Mockito.anyString(), Mockito.anyList());

        // Act
        try {
            Affectation nameUpdate = new Affectation("name", "New Name");
            Affectation ageUpdate = new Affectation("age", 30);

            mockDAO.updateFields("my_table", "123", Arrays.asList(nameUpdate, ageUpdate));

            // Assert
            Mockito.verify(mockDAO).updateFields("my_table", "123", Arrays.asList(nameUpdate, ageUpdate));
            Assertions.assert(true, "UpdateFields method should be called successfully.");
        } catch (Exception e) {
            Assertions.fail("Exception should not be thrown.");
        }
    }

    @Test
    void testMainMethodWithSQLException() {
        // Arrange
        Mockito.doThrow(SQLException.class).when(mockDAO).updateFields(Mockito.anyString(), Mockito.anyString(), Mockito.anyList());

        // Act & Assert
        try {
            Affectation nameUpdate = new Affectation("name", "New Name");
            Affectation ageUpdate = new Affectation("age", 30);

            mockDAO.updateFields("my_table", "123", Arrays.asList(nameUpdate, ageUpdate));
            Assertions.fail("SQLException should be thrown.");
        } catch (SQLException e) {
            Assertions.assert(true, "SQLException was correctly thrown.");
        } catch (Exception e) {
            Assertions.fail("Unexpected exception type thrown.");
        }
    }

    @Test
    void testMainMethodWithNullConnection() {
        // Arrange
        mockDAO.connection = null;

        // Act & Assert
        try {
            Affectation nameUpdate = new Affectation("name", "New Name");
            Affectation ageUpdate = new Affectation("age", 30);

            mockDAO.updateFields("my_table", "123", Arrays.asList(nameUpdate, ageUpdate));
            Assertions.fail("Exception should be thrown due to null connection.");
        } catch (Exception e) {
            Assertions.assert(true, "Exception was correctly thrown due to null connection.");
        }
    }
}