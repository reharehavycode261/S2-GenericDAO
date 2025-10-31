package database.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * La classe DBConnection est responsable de gérer les connexions à la base de données.
 */
public class DBConnection {

    /**
     * Établit une connexion à la base de données en utilisant les paramètres fournis par Config.
     * 
     * @param config La configuration contenant l'URL, le nom d'utilisateur et le mot de passe.
     * @return Une connexion SQL à la base de données.
     * @throws SQLException en cas de problème d'accès à la base de données.
     */
    public Connection connect(Config config) throws SQLException {
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }
}