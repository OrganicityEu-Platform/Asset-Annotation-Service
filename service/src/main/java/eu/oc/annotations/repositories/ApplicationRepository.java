package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long> {

    Application findByUrn(String Urn);

}
