package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.TagDomain;
import org.springframework.data.repository.CrudRepository;

public interface TagDomainRepository extends CrudRepository<TagDomain, Long> {

    TagDomain findById(Long id);

    TagDomain findByUrn(String Urn);
}
