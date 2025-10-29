package database.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Database {

    private Cache<String, List<?>> cache;

    public Database() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    protected List<?> getFromCache(String query) {
        return cache.getIfPresent(query);
    }

    protected void addToCache(String query, List<?> result) {
        cache.put(query, result);
    }

    protected void invalidateCache(String query) {
        cache.invalidate(query);
    }

    protected void clearCache() {
        cache.invalidateAll();
    }

    // ... code existant ...

    // Rappel : Toute opération d'insertion, de mise à jour ou de suppression doit appeler clearCache() pour invalider les résultats mis en cache
}