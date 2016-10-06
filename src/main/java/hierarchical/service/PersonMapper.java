package hierarchical.service;

import java.util.Collection;
import java.util.List;

import hierarchical.entity.Person;

/**
 * 
 * @author Dejan
 *
 * @param <I> ResultSet obtained from storage
 * @param <O> Record ready to be saved to storage
 */
public interface PersonMapper<I,O> {
    public O serialize(Person person);

    public Collection<Person> deserializeList(I input);
    
    public I getAllAncestors(Long id, Integer limit);
    public I getAllDescendants(Long id, Integer limit);
    public I getChildren(Long id);
    public void save(O o);

    public void resetDB();

}
