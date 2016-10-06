package hierarchical.data.generator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import hierarchical.entity.Gender;
import hierarchical.entity.Person;
import hierarchical.service.PersonMapper;

/**
 * Generates useful data for testing purposes.
 * @author dejan
 *
 */
public class PersonGenerator {
	
	@SuppressWarnings("rawtypes")
	private PersonMapper mapper;
	
    Random r = new Random(100);
	
	// person population from which we select a parent
	private List<Person> population;
	
	private List<String> maleFirstNames = Arrays.asList("Ivan", "Jovan", "Petar", "Zika", "Milan" ,"Goran", "Nikola", "Milos", "Marko", "Borivoje");
	private List<String> femaleFirstNames = Arrays.asList("Ivana", "Jovana", "Petra", "Zivana", "Milena" ,"Gorana", "Nikolina", "Milica", "Mara", "Bojana");
	private List<String> lastNames = Arrays.asList("Ivanovic", "Jovanovic", "Petrovic", "Zivanski", "Milutinovic" ,"Grokic", "Belic", "Milic", "Maric", "Bajic");
	
	private Long idNum;

	@SuppressWarnings("rawtypes")
	public PersonGenerator(PersonMapper mapper, Long idNum) {
		super();
		this.mapper = mapper;
		population = new LinkedList<Person>();
		this.idNum = idNum;
	}

	/**
	 * Method that generates Person data. 
	 * 
	 * @param initPop - size for initial "antediluvian" population.
	 * @param target - population target size.
	 * @param step - size for each generation.
	 */
	public void generate(int initPop, int target, int step){
		
		int generatedTotal = initPopulation(initPop);
		
		while (generatedTotal < target){
			population = procreate(step);
			generatedTotal += step;
		}
		
	}

    private int initPopulation(int step) {
        for(int i = 0; i < step; i++){
            Person child = makeChild(r, null, null);
            population.add(child);
        }
        return population.size();
    }

	private List<Person> procreate(int increase) {
		List<Person> youngPopulation = new LinkedList<Person>();

		while ((youngPopulation.size()) < increase ){
			int noOfChildren = r.nextInt(5);
			//find Father
			Person father = getPersonFromPop(Gender.MALE);
			//find Mother
			Person mother = getPersonFromPop(Gender.FEMALE);
			
			if(areNotCloseRelated(father, mother)){
				for(int i = 0; i < noOfChildren; i++){
					Person child = makeChild(r, father, mother);
					youngPopulation.add(child);
				}
			}
		}
		return youngPopulation;
	}

    private Person makeChild(Random r, Person father, Person mother) {
        String firstName;
        String lastName;
        Gender gender;
        
        if(father != null){
            lastName = father.getLastName();
        } else{
            lastName = lastNames.get(r.nextInt(lastNames.size()));
        }
        
        if(r.nextBoolean()){
        	firstName = maleFirstNames.get(r.nextInt(maleFirstNames.size()));
        	gender = Gender.MALE;
        } else {
        	firstName = femaleFirstNames.get(r.nextInt(femaleFirstNames.size()));
        	gender = Gender.FEMALE;
        }
        Person child = new Person(idNum++, firstName, lastName, gender, father, mother);
        mapper.save(mapper.serialize(child));
        return child;
    }
	
	private Person getPersonFromPop(Gender gender){
		
		while(true){
			Person person = population.get(r.nextInt(population.size() - 1));
			if(gender.equals(person.getGender())){
				return person;
			}
		}
	}
	
	/**
	 * Relatives check
	 * @param a
	 * @param b
	 */
	private boolean areNotCloseRelated(Person a, Person b) {
		Set<Long> ancestorIdsA = getAncestorIDs(a);
		Set<Long> ancestorIdsB = getAncestorIDs(b);
		ancestorIdsA.retainAll(ancestorIdsB);
		return ancestorIdsA.isEmpty();
	}

	private Set<Long> getAncestorIDs(Person a) {
		Set<Long> ancestorIds = new HashSet<Long>();
		if (a.getFather()!= null){
			ancestorIds.addAll(a.getFather().getIds());
		}
		if (a.getMother()!= null){
			ancestorIds.addAll(a.getMother().getIds());
		}
		return ancestorIds;
	}
	
}
