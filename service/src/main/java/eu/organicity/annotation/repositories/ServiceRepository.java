package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Service;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service, Long> {

    Service findByUrn(String Urn);

}
