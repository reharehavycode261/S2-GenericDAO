import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GenericDAOTest {

    @Mock
    private Database mockDatabase;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private TestDAO testDao;

    private static class TestEntity {
        private int id;
        private String name;

        public TestEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private static class TestDAO extends GenericDAO<TestEntity> {
        public TestDAO(Database database) {
            super(database, "test_table");
        }

        @Override
        protected TestEntity createEntity(ResultSet rs) throws SQLException {
            return new TestEntity(
                rs.getInt("id"),
                rs.getString("name")
            );
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        testDao = new TestDAO(mockDatabase);
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    @DisplayName("findAll should return empty list when no results")
    void findAll_NoResults_ReturnsEmptyList() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        List<TestEntity> results = testDao.findAll();

        assertTrue(results.isEmpty());
        verify(mockResultSet).next();
    }

    @Test
    @DisplayName("findAll should return list of entities when results exist")
    void findAll_WithResults_ReturnsEntityList() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("Test1", "Test2");

        List<TestEntity> results = testDao.findAll();

        assertEquals(2, results.size());
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getString("name");
    }

    @Test
    @DisplayName("count should return total number of records")
    void count_NoWhereClause_ReturnsTotalCount() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(10L);

        long count = testDao.count();

        assertEquals(10L, count);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    @DisplayName("count with where clause should return filtered count")
    void count_WithWhereClause_ReturnsFilteredCount() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        long count = testDao.count("name = 'Test'");

        assertEquals(5L, count);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    @DisplayName("findAll should throw SQLException when database error occurs")
    void findAll_DatabaseError_ThrowsSQLException() throws SQLException {
        when(mockDatabase.getConnection()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> testDao.findAll());
    }

    @Test
    @DisplayName("count should throw SQLException when database error occurs")
    void count_DatabaseError_ThrowsSQLException() throws SQLException {
        when(mockDatabase.getConnection()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> testDao.count());
    }

    @Test
    @DisplayName("count with empty where clause should behave like count without where clause")
    void count_EmptyWhereClause_BehavesLikeNoWhereClause() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(10L);

        long count1 = testDao.count("");
        long count2 = testDao.count("  ");
        long count3 = testDao.count(null);

        assertEquals(10L, count1);
        assertEquals(10L, count2);
        assertEquals(10L, count3);
    }
}