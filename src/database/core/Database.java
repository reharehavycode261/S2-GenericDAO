package database.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.concurrent.TimeUnit;

public abstract class Database {

    private Cache<String, Object> cache;
    
    public Database() {
        this.cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();
    }

    // Example method utilizing the cache
    public Object query(String query) throws SQLException {
        // Attempting to retrieve the result from cache
        Object result = cache.getIfPresent(query);
        if(result != null) {
            return result;
        }
      
        // Execute query since not present in cache
        result = executeQuery(query);
        
        // Store in cache
        cache.put(query, result);
        return result;
    }
    
    // Mock method to illustrate query execution
    private Object executeQuery(String query) throws SQLException {
        // Implement actual JDBC query execution here
        return new Object();  // Placeholder for real query result
    }

    // ... reste du code existant ...
}