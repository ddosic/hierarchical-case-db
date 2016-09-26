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

	public void generate(int target){
		int generatedTotal = 0;
		while (generatedTotal < target){
			int step = 100;
			procreate(step);
			generatedTotal += step;
		}
	}

	private void procreate(int increase) {
		List<Person> youngPopulation = new LinkedList<Person>();
		Random r = new Random();
		while ((youngPopulation.size()) < increase ){
			int noOfChildren = r.nextInt(5);
			//find Father
			Person father = getPersonFromPop(Gender.MALE, r);
			//find Mother
			Person mother = getPersonFromPop(Gender.FEMALE, r);
			
			if(areNotCloseRelated(father, mother)){
				for(int i = 0; i < noOfChildren; i++){
					String firstName;
					Gender gender;
					if(r.nextBoolean()){
						firstName = maleFirstNames.get(r.nextInt(lastNames.size()));
						gender = Gender.MALE;
						
					} else {
						firstName = femaleFirstNames.get(r.nextInt(lastNames.size()));
						gender = Gender.FEMALE;
					}
					Person child = new Person(idNum++, firstName, lastNames.get(r.nextInt(lastNames.size())), gender, father, mother);
					mapper.save(mapper.serialize(child));
					youngPopulation.add(child);
				}
			}
		}
		
	}
	
	private Person getPersonFromPop(Gender gender, Random r){
		
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
