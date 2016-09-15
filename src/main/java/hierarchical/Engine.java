package hierarchical;

import java.util.List;

import hierarchical.entity.Person;
import hierarchical.service.PersonMapper;
import hierarchical.service.mongo.PersonBsonMapperEngine;

import com.mongodb.MongoClient;

public class Engine {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();
        @SuppressWarnings("rawtypes")
        PersonMapper mapper = new PersonBsonMapperEngine(mongoClient);
        mapper.resetDB();
        mapper.save(mapper.serialize(new Person(1, "Petar", "Petrovic", new Person(11), new Person(12))));
        mapper.save(mapper.serialize(new Person(2, "Mara", "Petrovic", new Person(13), new Person(32))));
        mapper.save(mapper.serialize(new Person(3, "Mika", "Petrovic", new Person(1), new Person(2))));
        mapper.save(mapper.serialize(new Person(4, "Mika2", "Petrovic", new Person(3), new Person(22))));
        List<Person> persons = mapper.deserializeList(mapper.getAllAncestors((long) 4));
        List<Person> persons2 = mapper.deserializeList(mapper.getChildren((long) 1));
        System.out.println(persons);
        System.out.println(persons2);
        mongoClient.close();
    }

}
