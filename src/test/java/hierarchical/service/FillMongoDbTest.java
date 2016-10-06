package hierarchical.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hierarchical.data.generator.PersonGenerator;
import hierarchical.service.mongo.PersonBsonMapperEngine;

import com.mongodb.MongoClient;

public class FillMongoDbTest {
    MongoClient mongoClient;
    PersonGenerator dataGenerator;
    
    @SuppressWarnings("rawtypes")
    
    PersonMapper mapper; 
    
    @Before
    public  void init(){
        mongoClient  = new MongoClient();
    }
    
    @Test
    public void fill10000(){
        fill(10000,"persons10000");
    }
    
    @Test
    public void fill50000(){
        fill(50000,"persons50000");
    }
    
    @Test
    public void fill100000(){
        fill(100000,"persons100000");
    }
    
    @Test
    public void fill200000(){
        fill(200000,"persons200000");
    }
    
    @Test
    public void fill300000(){
       fill(300000,"persons300000");
    }

    private void fill(int i, String collName) {
        mapper = new PersonBsonMapperEngine(mongoClient, collName);
        dataGenerator = new PersonGenerator(mapper, 1l);
        mapper.resetDB();
        dataGenerator.generate(i/ 100, i, i / 10);
    }
    
    @After
    public void finish(){
        mongoClient.close();
    }
}
