package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByUrn(String Urn);

    Set<Tag> findAllByUrn(String Urn);

}
