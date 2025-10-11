package test;

import database.core.Database;
import database.core.GenericDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpDAO extends GenericDAO<Emp> {
    
    public EmpDAO(Database database) {
        super(database, "emp");
    }
    
    @Override
    protected Emp createEntity(ResultSet rs) throws SQLException {
        Emp emp = new Emp();
        emp.setNom(rs.getString("nom"));
        emp.setPrenom(rs.getString("prenom"));
        return emp;
    }
}