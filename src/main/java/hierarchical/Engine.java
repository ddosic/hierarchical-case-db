package hierarchical;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import hierarchical.entity.Person;
import hierarchical.service.PersonMapper;
import hierarchical.service.mongo.PersonBsonMapperEngine;
import hierarchical.service.sql.PersonSQLMapperEngine;

import com.mongodb.MongoClient;

public class Engine {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws SQLException {
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException exception) {
            System.out.println("Oracle Driver Class Not found Exception: " + exception.toString());
            return;
        }
        

        Connection jdbcConn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ddosic", "dejan");
        //create table osoba(id integer, first_name varchar2(20), last_name varchar2(20), father_id integer, mother_id integer, constraint osoba_pk primary key (id));
        
        MongoClient mongoClient = new MongoClient();
        @SuppressWarnings("rawtypes")
        PersonMapper mapper2 = new PersonBsonMapperEngine(mongoClient);
        
        @SuppressWarnings("rawtypes")
        PersonMapper mapper = new PersonSQLMapperEngine(jdbcConn);

        mapper.resetDB();
        mapper.save(mapper.serialize(new Person(1, "Petar", "Petrovic", new Person(11), new Person(12))));
        mapper.save(mapper.serialize(new Person(2, "Mara", "Petrovic", new Person(13), new Person(32))));
        mapper.save(mapper.serialize(new Person(3, "Mika", "Petrovic", new Person(1), new Person(2))));
        mapper.save(mapper.serialize(new Person(4, "Mika2", "Petrovic", new Person(3), new Person(22))));
        List<Person> persons = mapper.deserializeList(mapper.getAllDescendants((long) 1));
        List<Person> persons2 = mapper.deserializeList(mapper.getChildren((long) 1));
        List<Person> persons3 = mapper.deserializeList(mapper.getAllAncestors((long) 4));

        System.out.println(persons);
        System.out.println(persons2);
        System.out.println(persons3);
        mongoClient.close();
        jdbcConn.close();
    }

}
