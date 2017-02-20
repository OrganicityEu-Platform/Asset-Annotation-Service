package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Service;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service, Long> {

    Service findByUrn(String Urn);

}
