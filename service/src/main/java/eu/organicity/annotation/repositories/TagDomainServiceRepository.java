package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Service;
import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.domain.TagDomainService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagDomainServiceRepository extends CrudRepository<TagDomainService, Long> {
    
    @Cacheable(value = "TagDomainServiceCache")
    Iterable<TagDomainService> findAll();
    
    @Cacheable(value = "TagDomainServiceCache")
    List<TagDomainService> findByTagDomain(TagDomain tagDomain);
    
    @Cacheable(value = "TagDomainServiceCache")
    List<TagDomainService> findByService(Service service);
    
    TagDomainService findByServiceAndTagDomain(Service service, TagDomain tagDomain);
    
    
    @CacheEvict(value = "TagDomainServiceCache", allEntries = true)
    <S extends TagDomainService> S save(S entity);
    
    @CacheEvict(value = "TagDomainServiceCache", allEntries = true)
    void delete(Long id);
}
