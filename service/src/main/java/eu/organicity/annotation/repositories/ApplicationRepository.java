package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long> {

    Application findByUrn(String Urn);

}
