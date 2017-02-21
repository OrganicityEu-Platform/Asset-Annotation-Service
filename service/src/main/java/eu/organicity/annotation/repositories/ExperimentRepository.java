package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Experiment;
import org.springframework.data.repository.CrudRepository;

public interface ExperimentRepository extends CrudRepository<Experiment, Long> {

    Experiment findByUrn(String Urn);

}
