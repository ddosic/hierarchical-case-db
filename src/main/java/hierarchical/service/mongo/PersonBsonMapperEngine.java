package hierarchical.service.mongo;

import hierarchical.entity.Gender;
import hierarchical.entity.Person;
import hierarchical.service.PersonMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

/**
 * Concrete Person Mapper engine for working with MongoDB.
 * @author Dejan
 *
 */
public class PersonBsonMapperEngine implements PersonMapper<FindIterable<Document>, Document>{

    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";
    private static final String ANCESTORS = "ancestors";
    private static final String _ID = "_id";
	private static final String GENDER = "gender";
    MongoClient mongoClient;
    
    public PersonBsonMapperEngine(MongoClient mongoClient) {
        super();
        this.mongoClient = mongoClient;
    }

    /*
     * (non-Javadoc)
     * @see hierarchical.service.PersonMapper#serialize(hierarchical.entity.Person)
     */
    public Document serialize(Person person) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(_ID, person.getId());
        map.put(FIRST_NAME, person.getFirstName());
        map.put(LAST_NAME, person.getLastName());
        map.put(GENDER, person.getGender().name());

        List<Long> ancestors = new ArrayList<Long>();
        if (person.getFather() != null) {
            ancestors.add(person.getFather().getId());
        }
        if (person.getMother() != null) {
            ancestors.add(person.getMother().getId());
        }
        map.put(ANCESTORS, ancestors);

        return new Document(map);
    }

    /*
     * (non-Javadoc)
     * @see hierarchical.service.PersonMapper#deserializeList(java.lang.Object)
     */
    public List<Person> deserializeList(FindIterable<Document> t) {
        List<Person> persons = new ArrayList<Person>();
        MongoCursor<Document> cur = t.iterator();
        while (cur.hasNext()){
            Document doc = cur.next();
            persons.add(new Person(doc.getLong(_ID), doc.getString(FIRST_NAME), doc.getString(LAST_NAME), Gender.valueOf(doc.getString(GENDER))));
        }
        return persons;
    }

    private FindIterable<Document> getResults(Bson query) {
        
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> coll = db.getCollection("abba");
        FindIterable<Document> docs = coll.find(query);
        return docs;
    }
    
    public FindIterable<Document> getChildren(Long id) {
        return getResults(or (eq(ANCESTORS + ".0", id), eq(ANCESTORS + ".1", id)));
    }
    
    @SuppressWarnings("unchecked")
    public FindIterable<Document> getAllAncestors(Long id) {
        Document doc =  getResults(eq("_id", id)).first();
        return getResults(in("_id", (ArrayList<Long>) doc.get(ANCESTORS)));
    }

    public FindIterable<Document> getAllDescendants(Long id) {
        return getResults(in(ANCESTORS, id));
    }
    
    @SuppressWarnings("unchecked")
    public void save(Document o) {
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> coll = db.getCollection("abba");
        List<Long> ancestors = (List<Long>) o.get(ANCESTORS);
        if(ancestors.size() > 0){
            MongoCursor<Document> cur = coll.find(in("_id",ancestors)).iterator();
            while (cur.hasNext()){
                Document parent =  cur.next();
                ancestors.addAll((Collection<Long>) parent.get(ANCESTORS));
            }
            o.put(ANCESTORS, ancestors);
        }
        coll.insertOne(o);
    }

    public void resetDB() {
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> coll = db.getCollection("abba");
        coll.deleteMany(new Document());
    }
}
