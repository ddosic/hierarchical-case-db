package hierarchical.service;

import hierarchical.data.generator.PersonGenerator;
import hierarchical.service.PersonMapper;
import hierarchical.service.mongo.PersonBsonMapperEngine;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;


public class PersonBsonMapperTest {
    
    static Logger log = LoggerFactory.getLogger(PersonBsonMapperTest.class);
    
    static MongoClient mongoClient;
    static PersonGenerator dataGenerator;
    
    @SuppressWarnings("rawtypes")
    static
    PersonMapper mapper; 
    
    @BeforeClass
    public static void initData(){
        mongoClient  = new MongoClient();
        mapper = new PersonBsonMapperEngine(mongoClient);
        dataGenerator = new PersonGenerator(mapper, 1l);
        log.info("Mongo collection preparation start.");      
        long mils = System.currentTimeMillis();
        //mapper.resetDB();
        //dataGenerator.generate(1000, 10000, 1000);
        log.info("Mongo collection preparation end. Duration : " + (System.currentTimeMillis() - mils) + "ms"); 
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
        System.out.println((mapper.deserializeList(res)).size());
    }
    @AfterClass
    public static void finish(){
        mongoClient.close();
    }

}
