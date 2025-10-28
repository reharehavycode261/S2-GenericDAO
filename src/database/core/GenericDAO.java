package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class GenericDAO<T> {
    String id;
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Récupère les enregistrements de la table de manière paginée
     * @param pageNumber Le numéro de la page à récupérer (commençant à 1)
     * @param pageSize Le nombre d'éléments par page
     * @return La liste des enregistrements sous forme d'objets
     * @throws SQLException en cas d'erreur SQL
     */
    public List<T> findWithPagination(int pageNumber, int pageSize) throws SQLException {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and page size must be greater than 0");
        }

        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                List<T> results = new ArrayList<>();
                // Supposons que la méthode resultSetToObject convertirait un ResultSet en instance T
                while (rs.next()) {
                    T obj = resultSetToObject(rs);
                    results.add(obj);
                }
                return results;
            }
        }
    }
    
    // Exemple d'une méthode de conversion ResultSet vers Objet
    private T resultSetToObject(ResultSet rs) throws SQLException {
        // L'implémentation ici dépendrait de la structure de T
        // Exemple fictif, à ajuster selon le besoin réel
        return (T) new Object(); 
    }

    // ... le reste du code existant ...
}