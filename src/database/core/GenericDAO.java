package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GenericDAO<T> {

    protected Connection connection;
    protected String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }
    
    /**
     * Met à jour des champs spécifiques pour un enregistrement de la table
     * @param id L'identifiant de l'enregistrement
     * @param fields Liste des paires champ-valeur à mettre à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(int id, List<Affectation> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("La liste des champs à mettre à jour ne peut pas être vide");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");

        for (int i = 0; i < fields.size(); i++) {
            Affectation field = fields.get(i);
            sql.append(field.getColumn()).append(" = ?");
            if (i < fields.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Affectation field : fields) {
                stmt.setObject(index++, field.getValue());
            }
            stmt.setInt(index, id);

            stmt.executeUpdate();
        }
    }
}