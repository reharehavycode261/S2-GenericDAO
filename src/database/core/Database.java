package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

public abstract class Database {

    // Cache pour stocker les résultats fréquents
    private Map<String, Object> cache = new ConcurrentHashMap<>();

    protected Object getCachedResult(String key) {
        return cache.get(key);
    }

    protected void setCachedResult(String key, Object value) {
        cache.put(key, value);
    }

    protected void invalidateCache(String key) {
        cache.remove(key);
    }

    protected void clearAllCache() {
        cache.clear();
    }

    // ... rest of the class ...
}