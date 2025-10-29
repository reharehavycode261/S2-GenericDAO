package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GenericDAO {
    String id;
    private ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    public void createTable(DBConnection dbConnection) throws SQLException, AttributeMissingException {
        // Implémentation existante
    }
    
    public Object findById(DBConnection dbConnection, String id) throws SQLException {
        if (cache.containsKey(id)) {
            return cache.get(id);
        } else {
            // Simulation de la récupération dans la base de données
            Object result = performDatabaseQueryById(id);
            cache.put(id, result);
            return result;
        }
    }

    public void insert(DBConnection dbConnection, Object item) throws SQLException {
        // Insertion de l'élément dans la base de données
        performDatabaseInsert(item);
        cache.clear(); // Invalidation du cache après insertion
    }

    public void update(DBConnection dbConnection, String id, Object item) throws SQLException {
        // Mise à jour de l'élément dans la base de données
        performDatabaseUpdate(id, item);
        cache.clear(); // Invalidation du cache après mise à jour
    }

    public void delete(DBConnection dbConnection, String id) throws SQLException {
        // Suppression de l'élément dans la base de données
        performDatabaseDelete(id);
        cache.clear(); // Invalidation du cache après suppression
    }

    private Object performDatabaseQueryById(String id) {
        // Simulateur de requête, remplacez par une vraie requête
        return new Object();
    }

    private void performDatabaseInsert(Object item) {
        // Simulateur d'insertion, remplacez par une vraie insertion
    }

    private void performDatabaseUpdate(String id, Object item) {
        // Simulateur de mise à jour, remplacez par une vraie mise à jour
    }

    private void performDatabaseDelete(String id) {
        // Simulateur de suppression, remplacez par une vraie suppression
    }
}