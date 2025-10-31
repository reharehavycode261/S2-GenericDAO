package database.core;

import java.sql.*;
import java.util.*;

/**
 * GenericDAO est une classe générique pour les opérations CRUD sur une base de données.
 * @param <T> Le type d'objet sur lequel opère le DAO.
 */
public class GenericDAO<T> {

    // Connexion à la base de données.
    protected Connection connection;
    // Nom de la table associée au type générique T.
    protected String tableName;
    
    /**
     * Constructeur permettant d'associer un DAO à une connexion et une table spécifique.
     * 
     * @param connection La connexion à utiliser pour les opérations SQL.
     * @param tableName  Le nom de la table dans la base de données.
     */
    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }
    
    /**
     * Compte le nombre total d'enregistrements dans la table.
     * @return Le nombre d'enregistrements.
     * @throws SQLException en cas d'erreur SQL.
     */
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }
    
    // ... autres méthodes CRUD telles que create, update, delete ...
}