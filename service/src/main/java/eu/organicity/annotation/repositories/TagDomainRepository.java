package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.TagDomain;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TagDomainRepository extends CrudRepository<TagDomain, Long> {
    
    Iterable<TagDomain> findAll();
    
    TagDomain findById(Long id);
    
    Set<TagDomain> findByDescriptionContaining(String description);
    
    TagDomain findByUrn(String Urn);
    
    Set<TagDomain> findByUser(String user);
    
    <S extends TagDomain> S save(S entity);
    
    void delete(Long id);
    
    void delete(TagDomain tagDomain);
    
}
