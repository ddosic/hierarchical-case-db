package hierarchical.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import hierarchical.data.generator.PersonGenerator;
import hierarchical.entity.Person;
import hierarchical.service.PersonMapper;
import hierarchical.service.mongo.PersonBsonMapperEngine;
import hierarchical.service.sql.PersonSQLMapperEngine;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;


public class PersonSqlMapperTest {
    
    Logger log = LoggerFactory.getLogger(PersonSqlMapperTest.class);
    
    Connection jdbcConn; 
    
    PersonGenerator dataGenerator;
    
    @SuppressWarnings("rawtypes")
    
    PersonMapper mapper; 
    
    @Before
    public void initData() throws SQLException{
        jdbcConn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ddosic", "dejan");
    }
    
    @Test(timeout =600000)
    public void test10000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA10000");
        testAll(10000);
    }
    @Test(timeout =600000)
    public void test50000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA50000");
        testAll(50000);
    }
    @Test(timeout =600000)
    public void test100000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA100000");
        testAll(100000);
    }
    @Test(timeout =600000)
    public void test200000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA200000");
        testAll(200000);
    }
    @Test(timeout =600000)
    public void test300000(){
        mapper = new PersonSQLMapperEngine(jdbcConn, "OSOBA300000");
        testAll(300000);
    }
    
    public void testAll(int datasetSize){
        log.info("Begin tests for Oracle - Dataset size: " + datasetSize);
        long mils = System.currentTimeMillis();
        MapperTestUtil.testGetAllAncestors(mapper, new Long(datasetSize));
        MapperTestUtil.testGetAllChildren(mapper, new Long(2));
        MapperTestUtil.testGetAllDescendants(mapper, new Long(2));
        MapperTestUtil.testGetFirstDescendants(mapper, new Long(2));
        MapperTestUtil.testGetFirstAncestors(mapper, new Long(datasetSize));
        log.info("End tests for Oracle - Dataset size: " + datasetSize);
    }

 
    @After
    public void finish() throws SQLException{
        jdbcConn.close();
    }

}
