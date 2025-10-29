import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatabaseTest {

    private Database database;

    @BeforeEach
    public void setUp() {
        database = Mockito.mock(Database.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testAddToCacheAndGetFromCache() {
        String query = "SELECT * FROM users";
        List<String> result = Arrays.asList("user1", "user2");

        database.addToCache(query, result);
        List<?> cachedResult = database.getFromCache(query);

        Assertions.assertNotNull(cachedResult, "Cache should return a non-null result");
        Assertions.assertEquals(result, cachedResult, "Cached result should match the original result");
    }

    @Test
    public void testGetFromCacheWhenEmpty() {
        String query = "SELECT * FROM non_existing_table";

        List<?> cachedResult = database.getFromCache(query);

        Assertions.assertNull(cachedResult, "Cache should return null for a query that was never cached");
    }

    @Test
    public void testInvalidateCache() {
        String query = "SELECT * FROM users";
        List<String> result = Arrays.asList("user1", "user2");

        database.addToCache(query, result);
        database.invalidateCache(query);
        List<?> cachedResult = database.getFromCache(query);

        Assertions.assertNull(cachedResult, "Cache should return null after invalidation");
    }

    @Test
    public void testClearCache() {
        String query1 = "SELECT * FROM users";
        String query2 = "SELECT * FROM orders";
        List<String> result1 = Arrays.asList("user1", "user2");
        List<String> result2 = Arrays.asList("order1", "order2");

        database.addToCache(query1, result1);
        database.addToCache(query2, result2);
        database.clearCache();

        List<?> cachedResult1 = database.getFromCache(query1);
        List<?> cachedResult2 = database.getFromCache(query2);

        Assertions.assertNull(cachedResult1, "Cache should return null for query1 after clearing");
        Assertions.assertNull(cachedResult2, "Cache should return null for query2 after clearing");
    }
}