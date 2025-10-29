package database.core;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant la base de données et fournissant des outils pour les opérations courantes.
 */
public abstract class Database {

    // Methode pour obtenir une connexion à la base de données
    protected Connection getConnection() throws SQLException {
        // Détaillez ici la manière dont la connexion est obtenue.
        return null; // Implémentation de la connexion manquante
    }

    // Méthode pour exécuter une requête donnée.
    protected void executeQuery(String query) throws SQLException {
        // Logique pour exécuter une requête SQL.
    }
    // Autres méthodes utilitaires pour les opérations sur la base de données.
}