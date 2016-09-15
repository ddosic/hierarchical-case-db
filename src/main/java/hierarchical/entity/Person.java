package hierarchical.entity;

public class Person {
    
    private long id;
    private String firstName;
    private String lastName;
    private Person father;
    private Person mother;
    
    public Person() {
        super();
    }
    
    
    public Person(long id) {
        super();
        this.id = id;
    }

    public Person(long id, String firstName, String lastName) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public Person(long id, String firstName, String lastName, Person father, Person mother) {
        this(id, firstName, lastName);
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

    @Override
    public String toString() {
        return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}
