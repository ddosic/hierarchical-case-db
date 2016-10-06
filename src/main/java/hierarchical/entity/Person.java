package hierarchical.entity;

import java.util.HashSet;
import java.util.Set;

public class Person {
    
    private long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Person father;
    private Person mother;
    
    public Person() {
        super();
    }
    
    
    public Person(long id) {
        super();
        this.id = id;
    }

    public Person(long id, String firstName, String lastName, Gender gender) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }
    
    public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public Person(long id, String firstName, String lastName, Gender gender, Person father, Person mother) {
        this(id, firstName, lastName, gender);
        this.father = father;
        this.mother = mother;
    }
        
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Person getFather() {
		return father;
	}

	public void setFather(Person father) {
		this.father = father;
	}

	public Person getMother() {
		return mother;
	}

	public void setMother(Person mother) {
		this.mother = mother;
	}

	/**
	 * Helper method that returns id of person + ids of parents
	 * @return
	 */
	public Set<Long> getIds() {
		Set<Long> list = new HashSet<Long>();
		
		if (father != null) {
			list.add(father.id);
		}
		if (mother != null) {
			list.add(mother.id);
		}
		
		return list;
	}

    @Override
    public String toString() {
        return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
