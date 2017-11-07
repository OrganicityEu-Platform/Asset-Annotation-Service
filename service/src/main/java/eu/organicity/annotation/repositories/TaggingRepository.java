package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.Tagging;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaggingRepository extends CrudRepository<Tagging, Long> {
    
    long countByUrn(String urn);
    
    long countByUrnAndTag(String urn, Tag t);
    
    long countByTag(Tag t);
    
    @Query(value = "SELECT * FROM annotation where last_modified IS NOT NULL ORDER BY last_modified DESC LIMIT 1", nativeQuery = true)
    Tagging findFirstOrderByLastModifiedDesc();
    
    @Query(value = "SELECT * FROM annotation where created IS NOT NULL ORDER BY created ASC LIMIT 1", nativeQuery = true)
    Tagging findFirstOrderByCreatedAsc();
    
    @Query(value = "SELECT * FROM annotation where last_modified IS NOT NULL and urn= ?1 ORDER BY last_modified DESC LIMIT 1", nativeQuery = true)
    Tagging findFirstOrderByLastModifiedDesc(String urn);
    
    @Query(value = "SELECT * FROM annotation where created IS NOT NULL and urn= ?1 ORDER BY created ASC LIMIT 1", nativeQuery = true)
    Tagging findFirstOrderByCreatedAsc(String urn);
    
    //    @Query("select min(t.lastModifiedDate) from Tagging t where t.urn= ?1")
    //    Tagging findMaxLastModifiedDate(String urn);
    //
    //    @Query("select min(t.createdDate) from Tagging t where t.urn= ?1")
    //    Tagging findMinCreatedDate(String urn);
    
    Tagging findByUrnAndUserAndTag(String urn, String user, Tag tag);
    
    List<Tagging> findByUrn(String urn);
    
    @Query(value = "SELECT * FROM annotation where urn like ?1", nativeQuery = true)
    List<Tagging> findByUrnLike(String urn);
    
    List<Tagging> findByTag(Tag t);
    
    List<Tagging> findByUrnAndTag(String urn, Tag t);
}
