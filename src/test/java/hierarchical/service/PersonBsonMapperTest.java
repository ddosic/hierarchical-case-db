package hierarchical.service;

import hierarchical.data.generator.PersonGenerator;
import hierarchical.service.PersonMapper;
import hierarchical.service.mongo.PersonBsonMapperEngine;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;


public class PersonBsonMapperTest {
    
    Logger log = LoggerFactory.getLogger(PersonBsonMapperTest.class);
    
    MongoClient mongoClient;
    PersonGenerator dataGenerator;
    
    @SuppressWarnings("rawtypes")
    
    PersonMapper mapper; 
    
    @Before
    public  void init(){
        mongoClient  = new MongoClient();
    }
    
    @Test(timeout =600000)
    public void test10000(){
        mapper = new PersonBsonMapperEngine(mongoClient, "persons10000");
        testAll(10000);
    }
    @Test(timeout =600000)
    public void test50000(){
        mapper = new PersonBsonMapperEngine(mongoClient, "persons50000");
        testAll(50000);
    }
    @Test(timeout =600000)
    public void test100000(){
        mapper = new PersonBsonMapperEngine(mongoClient, "persons100000");
        testAll(100000);
    }
    @Test(timeout =600000)
    public void test200000(){
        mapper = new PersonBsonMapperEngine(mongoClient, "persons200000");
        testAll(200000);
    }
    @Test(timeout =600000)
    public void test300000(){
        mapper = new PersonBsonMapperEngine(mongoClient, "persons300000");
        testAll(300000);
    }
    public void testAll(int datasetSize){
        log.info("Begin tests for Mongo - Dataset size: " + datasetSize);
        MapperTestUtil.testGetAllAncestors(mapper, new Long(datasetSize));
        MapperTestUtil.testGetAllChildren(mapper, new Long(2));
        MapperTestUtil.testGetAllDescendants(mapper, new Long(2));
        MapperTestUtil.testGetFirstDescendants(mapper, new Long(2));
        MapperTestUtil.testGetFirstAncestors(mapper, new Long(datasetSize));
        log.info("End tests for Mongo - Dataset size: " + datasetSize);
    }
 
    @After
    public void finish(){
        mongoClient.close();
    }

}
