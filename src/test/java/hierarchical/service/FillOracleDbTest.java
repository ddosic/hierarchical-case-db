package hierarchical.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hierarchical.data.generator.PersonGenerator;
import hierarchical.service.mongo.PersonBsonMapperEngine;
import hierarchical.service.sql.PersonSQLMapperEngine;

import com.mongodb.MongoClient;

public class FillOracleDbTest {
    
    Connection jdbcConn; 
    PersonGenerator dataGenerator;
    
    @SuppressWarnings("rawtypes")
    
    PersonMapper mapper; 
    
    @Before
    public  void init() throws SQLException{
        jdbcConn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ddosic", "dejan");
    }
    
    @Test
    public void fill10000(){
       mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA10000");
       dataGenerator = new PersonGenerator(mapper, 1l);
       mapper.resetDB();
       dataGenerator.generate(100, 10000, 1000);
    }
    
    @Test
    public void fill50000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA50000");
        dataGenerator = new PersonGenerator(mapper, 1l);
        mapper.resetDB();
       dataGenerator.generate(500, 50000, 5000);
    }
    
    @Test
    public void fill100000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA100000");
       dataGenerator = new PersonGenerator(mapper, 1l);
       mapper.resetDB();
       dataGenerator.generate(1000, 100000, 10000);
    }
    
    @Test
    public void fill200000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA200000");
       dataGenerator = new PersonGenerator(mapper, 1l);
       mapper.resetDB();
       dataGenerator.generate(2000, 200000, 20000);
    }
    
    @Test
    public void fill300000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA300000");
       dataGenerator = new PersonGenerator(mapper, 1l);
       mapper.resetDB();
       dataGenerator.generate(3000, 300000, 30000);
    }
    
    @After
    public void finish() throws SQLException{
        jdbcConn.close();
    }
}
