package hierarchical.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import hierarchical.data.generator.PersonGenerator;
import hierarchical.service.PersonMapper;
import hierarchical.service.mongo.PersonBsonMapperEngine;
import hierarchical.service.sql.PersonSQLMapperEngine;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;


public class PersonSqlMapperTest {
    
    static Logger log = LoggerFactory.getLogger(PersonSqlMapperTest.class);
    
    static Connection jdbcConn; 
    
    static PersonGenerator dataGenerator;
    
    @SuppressWarnings("rawtypes")
    static
    PersonMapper mapper; 
    
    @BeforeClass
    public static void initData() throws SQLException{
        jdbcConn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ddosic", "dejan");
        mapper = new PersonSQLMapperEngine(jdbcConn);
        dataGenerator = new PersonGenerator(mapper, 1l);
        log.info("Oracle collection preparation start.");      
        long mils = System.currentTimeMillis();
        //mapper.resetDB();
        //dataGenerator.generate(1000, 10000, 1000);
        log.info("Oracle collection preparation end. Duration : " + (System.currentTimeMillis() - mils) + "ms"); 
    }
    
    @Test
    public void testGetAllDescendants(){
        log.info("testFindDescendants start.");      
        Object res = mapper.getAllDescendants(new Long(25));
        log.info("testFindDescendants end."); 
        System.out.println((mapper.deserializeList(res)).size());
    }
    
    @Test
    public void testGetAllChildren(){
        log.info("testGetAllChildren start.");      
        Object res = mapper.getChildren(new Long(25));
        log.info("testGetAllChildren end."); 
        System.out.println((mapper.deserializeList(res)).size());
    }
    
    @Test
    public void testGetAllAncestors(){
        log.info("testGetAllAncestors start.");      
        Object res = mapper.getAllAncestors(new Long(10000));
        log.info("testGetAllAncestors end."); 
     //   System.out.println((mapper.deserializeList(res)).size());
    }
    @AfterClass
    public static void finish() throws SQLException{
        jdbcConn.close();
    }

}
