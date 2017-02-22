package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.Tagging;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaggingRepository extends CrudRepository<Tagging, Long> {
    
    List<Tagging> findByUrn(String urn);
    
    List<Tagging> findByTag(Tag t);
}
