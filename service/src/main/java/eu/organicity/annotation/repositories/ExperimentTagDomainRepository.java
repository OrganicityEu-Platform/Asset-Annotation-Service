package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Experiment;
import eu.organicity.annotation.domain.ExperimentTagDomain;
import eu.organicity.annotation.domain.TagDomain;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExperimentTagDomainRepository extends CrudRepository<ExperimentTagDomain, Long> {
    
//    @Cacheable(value = "ExperimentTagDomainCache")
    Iterable<ExperimentTagDomain> findAll();
    
//    @Cacheable(value = "ExperimentTagDomainCache")
    List<ExperimentTagDomain> findByTagDomain(TagDomain tagDomain);
    
//    @Cacheable(value = "ExperimentTagDomainCache")
    List<ExperimentTagDomain> findByExperiment(Experiment experiment);
    
//    @Cacheable(value = "ExperimentTagDomainCache")
    ExperimentTagDomain findByExperimentAndTagDomain(Experiment service, TagDomain tagDomain);
    
//    @CacheEvict(value = "ExperimentTagDomainCache", allEntries = true)
    <S extends ExperimentTagDomain> S save(S entity);
    
//    @CacheEvict(value = "ExperimentTagDomainCache", allEntries = true)
    void delete(Long id);
    
//    @CacheEvict(value = "ExperimentTagDomainCache", allEntries = true)
    void delete(ExperimentTagDomain experimentTagDomain);
}
