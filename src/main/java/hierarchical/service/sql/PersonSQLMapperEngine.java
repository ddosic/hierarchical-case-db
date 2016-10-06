package hierarchical.service.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hierarchical.entity.Gender;
import hierarchical.entity.Person;
import hierarchical.service.PersonMapper;

/**
 * Concrete Person Mapper engine for working with Oracle RDBMS.
 * @author Dejan
 *
 */
public class PersonSQLMapperEngine implements PersonMapper<ResultSet, String>{
    
    Connection jdbcConn;
    String table;

    public PersonSQLMapperEngine(Connection jdbcConn, String table) {
        super();
        this.jdbcConn = jdbcConn;
        this.table = table;
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
        
        return "(" + person.getId() + ",'" + person.getFirstName() + "','" + person.getLastName()+ "'," + father + "," + mother +",'"+ person.getGender() + "')";
    }

    public Collection<Person> deserializeList(ResultSet input) {
        Set<Person> persons = new HashSet<Person>();
        try {
            while(input.next()){
                persons.add(new Person(input.getLong("id"), input.getString("first_name"), input.getString("last_name"), Gender.MALE));
            }
            input.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    public ResultSet getAllAncestors(Long id, Integer limit) {
        Statement stmt;
        try {
            stmt = jdbcConn.createStatement();
            
            String sql = "select distinct id, first_name,last_name, gender from (SELECT  id as idc,CONNECT_BY_ROOT id as id, CONNECT_BY_ROOT first_name as first_name,"
            		+ "  CONNECT_BY_ROOT last_name as last_name, CONNECT_BY_ROOT gender as gender FROM "
                    + " (select id, first_name,last_name,gender, father_id as parent_id from " + table
                    + " UNION ALL "
                    + " select id,first_name,last_name, gender, mother_id from " + table + ")"
                   + " CONNECT BY PRIOR id = parent_id)"
                   + " where idc = " + id;
            if(limit!=null){
                sql += " and ROWNUM <= " + limit;
            }
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getAllDescendants(Long id, Integer limit) {
        Statement stmt;
        try {
            stmt = jdbcConn.createStatement();
        String sql = "SELECT distinct id, first_name, last_name FROM "
                + " (select id, first_name,last_name,gender, father_id as parent_id from " + table
        		+ " UNION ALL "
        		+ " select id,first_name,last_name, gender, mother_id from " + table + ")"
                + " WHERE LEVEL > 1"
                + " START WITH id = " + id
                + " CONNECT BY PRIOR id = parent_id";
        if(limit!=null){
            sql += " and ROWNUM <= " + limit;
        }
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
        String sql = "SELECT distinct id, first_name, last_name FROM "
                + " (select id, first_name,last_name,gender, father_id as parent_id from " + table
        		+ " UNION ALL "
        		+ " select id,first_name,last_name, gender, mother_id from " + table+")"
                + " WHERE LEVEL = 2"
                + " START WITH id = " + id
                + " CONNECT BY PRIOR id = parent_id";
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
            stat.executeUpdate("insert into " + table + " values " + o);
            stat.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public void resetDB() {
        try {
            Statement stat = jdbcConn.createStatement();
            stat.execute("delete from " + table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

}
