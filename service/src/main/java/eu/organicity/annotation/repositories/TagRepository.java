package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.TagDomain;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TagRepository extends CrudRepository<Tag, Long> {
    
    @Cacheable(value = "TagCache")
    Iterable<Tag> findAll();
    
    Tag findByUrn(String urn);
    
    Set<Tag> findAllByUrn(String urn);
    
    List<Tag> findByTagDomain(TagDomain domain);
    
    @CacheEvict(value = "TagCache", allEntries = true)
    <S extends Tag> S save(S entity);
    
    @CacheEvict(value = "TagCache", allEntries = true)
    void delete(Long id);
}
