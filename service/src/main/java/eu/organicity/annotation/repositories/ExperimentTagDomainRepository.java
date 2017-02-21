package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Experiment;
import eu.organicity.annotation.domain.ExperimentTagDomain;
import eu.organicity.annotation.domain.TagDomain;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExperimentTagDomainRepository extends CrudRepository<ExperimentTagDomain, Long> {

    List<ExperimentTagDomain> findByTagDomain(TagDomain tagDomain);

    List<ExperimentTagDomain> findByExperiment(Experiment experiment);
    
    ExperimentTagDomain findByExperimentAndTagDomain(Experiment service, TagDomain tagDomain);
}
