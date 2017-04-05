package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Service;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    
//    @Cacheable(value = "ServiceCache")
    Service findByUrn(String Urn);
    
    Set<Service> findByDescriptionContaining(String description);
    
//    @CacheEvict(value = "ServiceCache", allEntries = true)
    <S extends Service> S save(S entity);
    
//    @CacheEvict(value = "ServiceCache", allEntries = true)
    void delete(Long id);
    
//    @CacheEvict(value = "ServiceCache", allEntries = true)
    void delete(Service service);
}
