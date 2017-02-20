package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Tagging;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaggingRepository extends CrudRepository<Tagging, Long> {

    List<Tagging> findByUrn(String Urn);

}
