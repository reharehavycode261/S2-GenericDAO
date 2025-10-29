package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class GenericDAO<T> {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private Jedis jedis;

    public GenericDAO() {
        this.jedis = new Jedis(REDIS_HOST, REDIS_PORT);
    }
    
    public void createTable(DBConnection dbConnection) throws SQLException, AttributeMissingException, AttributeTypeNotExistingException {
        // Création de la logique du tableau
    }

    public List<T> findAll(DBConnection dbConnection) throws SQLException {
        String cacheKey = "all_data";
        String cachedResult = jedis.get(cacheKey);
        
        if (cachedResult != null) {
            // Logique pour convertir le résultat mis en cache en List<T>
            return convertFromCache(cachedResult);
        } else {
            List<T> result = fetchDataFromDB();
            jedis.set(cacheKey, convertToCache(result));
            return result;
        }
    }

    public T findById(DBConnection dbConnection, String id) throws SQLException, NotIdentifiedInDatabaseException {
        String cacheKey = "data_" + id;
        String cachedResult = jedis.get(cacheKey);

        if (cachedResult != null) {
            return convertFromCache(cachedResult);
        } else {
            T result = fetchByIdFromDB(id);
            jedis.set(cacheKey, convertToCache(result));
            return result;
        }
    }

    public void insert(DBConnection dbConnection, T obj) throws SQLException {
        // Logique d'insertion dans la DB
        invalidateCache();  // Invalide le cache après l'insertion
    }

    public void update(DBConnection dbConnection, T obj) throws SQLException {
        // Logique de mise à jour dans la DB
        invalidateCache();  // Invalide le cache après la mise à jour
    }

    public void delete(DBConnection dbConnection, T obj) throws SQLException {
        // Logique de suppression de la DB
        invalidateCache();  // Invalide le cache après la suppression
    }

    private void invalidateCache() {
        jedis.flushDB();  // Invalide l'ensemble du cache
    }

    private List<T> fetchDataFromDB() {
        // Dummy implementation for fetching from DB
        return null;
    }

    private T fetchByIdFromDB(String id) {
        // Dummy implementation for fetching by ID from DB
        return null;
    }

    private String convertToCache(Object obj) {
        // Dummy serialization logic
        return obj.toString();
    }

    private <T> T convertFromCache(String cachedData) {
        // Dummy deserialization logic
        return null;
    }
}