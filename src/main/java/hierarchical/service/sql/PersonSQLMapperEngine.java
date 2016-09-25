package hierarchical.service.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hierarchical.entity.Person;
import hierarchical.service.PersonMapper;

/**
 * Concrete Person Mapper engine for working with Oracle RDBMS.
 * @author Dejan
 *
 */
public class PersonSQLMapperEngine implements PersonMapper<ResultSet, String>{
    
    Connection jdbcConn;

    public PersonSQLMapperEngine(Connection jdbcConn) {
        super();
        this.jdbcConn = jdbcConn;
    }

    public String serialize(Person person) {
        String father = "null";
        String mother = "null";
        if (person.getFather()!= null){
            father = "" + person.getFather().getId();
        }
        if (person.getMother()!= null){
            mother = "" + person.getMother().getId();
        }
        
        return "(" + person.getId() + ",'" + person.getFirstName() + "','" + person.getLastName()+ "'," + father + "," + mother + ")";
    }

    public List<Person> deserializeList(ResultSet input) {
        List<Person> persons = new ArrayList<Person>();
        try {
            while(input.next()){
                persons.add(new Person(input.getLong("id"), input.getString("first_name"), input.getString("last_name")));
            }
            input.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return persons;
    }

    public ResultSet getAllAncestors(Long id) {
        Statement stmt;
        try {
            stmt = jdbcConn.createStatement();
            String sql = "SELECT CONNECT_BY_ROOT id as id, CONNECT_BY_ROOT first_name as first_name, "
                    + " CONNECT_BY_ROOT last_name as last_name FROM Osoba "
                    + " WHERE id = " + id
                    + " CONNECT BY PRIOR id = father_id or PRIOR id = mother_id";
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getAllDescendants(Long id) {
        Statement stmt;
        try {
            stmt = jdbcConn.createStatement();
        String sql = "SELECT id, first_name, last_name FROM Osoba "
                + " WHERE LEVEL > 1"
                + " START WITH id = " + id
                + " CONNECT BY PRIOR id = father_id or PRIOR id = mother_id";
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getChildren(Long id) {
        Statement stmt;
        try {
            stmt = jdbcConn.createStatement();
        String sql = "SELECT id, first_name, last_name FROM Osoba "
                + " WHERE LEVEL = 2"
                + " START WITH id = " + id
                + " CONNECT BY PRIOR id = father_id or PRIOR id = mother_id";
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String o) {
        try {
            Statement stat = jdbcConn.createStatement();
            stat.executeUpdate("insert into osoba values " + o);
            stat.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public void resetDB() {
        try {
            Statement stat = jdbcConn.createStatement();
            stat.execute("delete from Osoba");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

}
