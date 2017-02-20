package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.TagDomain;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByUrn(String Urn);

    Set<Tag> findAllByUrn(String Urn);

    List<Tag> findByTagDomain(TagDomain domain);
}
