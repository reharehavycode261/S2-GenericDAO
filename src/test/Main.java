package test;

import database.core.Config;
import database.core.GenericDAO;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Exemple d'utilisation avec une classe qui hérite de GenericDAO
            PlatDAO platDAO = new PlatDAO(Config.getPgDb().getConnection());

            // Comptage sans condition
            long countAll = platDAO.count(null);
            System.out.println("Total count: " + countAll);

            // Comptage avec condition
            long countWithCondition = platDAO.count("libelle LIKE 'Salad%'");
            System.out.println("Count with condition: " + countWithCondition);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class PlatDAO extends GenericDAO<Plat> {
    public PlatDAO(Connection connection) {
        this.connection = connection;
        this.tableName = "Plat"; // Nom de la table dans la base de données
    }
}