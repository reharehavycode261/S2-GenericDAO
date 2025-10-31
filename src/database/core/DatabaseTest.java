package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.ConcurrentHashMap;

class DatabaseTest {

    private Database database;

    // Classe concrète pour tester la classe abstraite Database
    private class TestDatabase extends Database {}

    @BeforeEach
    void setUp() {
        database = new TestDatabase();
    }

    @AfterEach
    void tearDown() {
        database.clearAllCache();
    }

    @Test
    void testGetCachedResultWhenKeyExists() {
        // Arrange
        String key = "testKey";
        String expectedValue = "testValue";
        database.setCachedResult(key, expectedValue);

        // Act
        Object actualValue = database.getCachedResult(key);

        // Assert
        Assertions.assertEquals(expectedValue, actualValue, "The cached value should be returned when the key exists.");
    }

    @Test
    void testGetCachedResultWhenKeyDoesNotExist() {
        // Arrange
        String key = "nonExistentKey";

        // Act
        Object actualValue = database.getCachedResult(key);

        // Assert
        Assertions.assertNull(actualValue, "The result should be null when the key does not exist in the cache.");
    }

    @Test
    void testSetCachedResult() {
        // Arrange
        String key = "newKey";
        String value = "newValue";

        // Act
        database.setCachedResult(key, value);
        Object cachedValue = database.getCachedResult(key);

        // Assert
        Assertions.assertEquals(value, cachedValue, "The value should be correctly set in the cache.");
    }

    @Test
    void testInvalidateCache() {
        // Arrange
        String key = "keyToInvalidate";
        String value = "valueToInvalidate";
        database.setCachedResult(key, value);

        // Act
        database.invalidateCache(key);
        Object cachedValue = database.getCachedResult(key);

        // Assert
        Assertions.assertNull(cachedValue, "The cache should be invalidated and return null for the invalidated key.");
    }

    @Test
    void testClearAllCache() {
        // Arrange
        database.setCachedResult("key1", "value1");
        database.setCachedResult("key2", "value2");

        // Act
        database.clearAllCache();
        Object cachedValue1 = database.getCachedResult("key1");
        Object cachedValue2 = database.getCachedResult("key2");

        // Assert
        Assertions.assertNull(cachedValue1, "The cache should be cleared and return null for any key.");
        Assertions.assertNull(cachedValue2, "The cache should be cleared and return null for any key.");
    }
}