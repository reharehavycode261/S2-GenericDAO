package database.core;

/**
 * La classe Config gère les configurations pour la connexion à la base de données.
 * Elle stocke des paramètres tels que l'URL, le nom d'utilisateur et le mot de passe.
 */
public class Config {
    
    // L'emplacement de la base de données.
    private String url;
    // Le nom d'utilisateur pour accéder à la base de données.
    private String username;
    // Le mot de passe associé à ce nom d'utilisateur.
    private String password;
    
    /**
     * Constructeur pour initialiser les paramètres de connexion.
     * 
     * @param url l'URL de connexion à la base de données.
     * @param username le nom d'utilisateur.
     * @param password le mot de passe.
     */
    public Config(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Getters pour accéder aux informations de configuration.
    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Définir d'autres méthodes utiles pour la configuration si nécessaire.
}