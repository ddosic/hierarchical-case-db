package hierarchical.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapperTestUtil {
    
    static Logger log = LoggerFactory.getLogger(MapperTestUtil.class);
    @SuppressWarnings("unchecked")
    public static void testGetAllChildren(PersonMapper mapper, Long id){
        long mils = logStart(mapper.getClass().getName() + " testGetAllChildren");
        Collection l = mapper.deserializeList(mapper.getChildren(id));
        logEnd(mils, l, mapper.getClass().getName() + " testGetAllChildren"); 
    }

    @SuppressWarnings("unchecked")
    public static void testGetAllDescendants(PersonMapper mapper, Long id){
        long mils = logStart(mapper.getClass().getName() + " testGetAllDescendants");
        Collection l = mapper.deserializeList(mapper.getAllDescendants(id, null));
        logEnd(mils, l, mapper.getClass().getName() + " testGetAllDescendants"); 
    }

    @SuppressWarnings("unchecked")
    public static void testGetAllAncestors(PersonMapper mapper, Long id){
        long mils = logStart(mapper.getClass().getName() + " testGetAllAncestors");
        Collection l =mapper.deserializeList(mapper.getAllAncestors(id, null));
        logEnd(mils, l, mapper.getClass().getName() + " testGetAllAncestors"); 
    }
    
    @SuppressWarnings("unchecked")
    public static void testGetFirstDescendants(PersonMapper mapper, Long id){
        long mils = logStart(mapper.getClass().getName() + " testGetFirst100Descendants");
        Collection l = mapper.deserializeList(mapper.getAllDescendants(id, 100));
        logEnd(mils, l, mapper.getClass().getName() + " testGetFirst100Descendants"); 
    }

    @SuppressWarnings("unchecked")
    public static void testGetFirstAncestors(PersonMapper mapper, Long id){
        long mils = logStart(mapper.getClass().getName() + " testGetFirst100Ancestors");
        Collection l =mapper.deserializeList(mapper.getAllAncestors(id, 100));
        logEnd(mils, l, mapper.getClass().getName() + " testGetFirst100Ancestors"); 
    }
    
    private static void logEnd(long mils, Collection l, String methodName) {
        log.info(methodName + " end after " + (System.currentTimeMillis() - mils) + " ms. Results size: " + l.size() );
    }

    private static long logStart(String methodName) {
        log.info(methodName +  " start.");    
        long mils = System.currentTimeMillis();
        return mils;
    }
}
