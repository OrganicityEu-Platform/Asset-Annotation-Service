package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.Tagging;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface TaggingRepository extends CrudRepository<Tagging, Long> {
    
    long countByUrn(String urn);
    
    long countByTag(Tag t);
    
    @Query("select max(t.lastModifiedDate) from Tagging t")
    ZonedDateTime findMaxLastModifiedDate();
    
    @Query("select min(t.createdDate) from Tagging t")
    ZonedDateTime findMinCreatedDate();
    
    @Query("select min(t.lastModifiedDate) from Tagging t where t.urn= ?1")
    ZonedDateTime findMaxLastModifiedDate(String urn);
    
    @Query("select min(t.createdDate) from Tagging t where t.urn= ?1")
    ZonedDateTime findMinCreatedDate(String urn);
    
    List<Tagging> findByUrn(String urn);
    
    List<Tagging> findByTag(Tag t);
}
