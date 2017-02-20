package eu.organicity.annotation.repositories;

import eu.organicity.annotation.domain.Service;
import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.domain.TagDomainService;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagDomainServiceRepository extends CrudRepository<TagDomainService, Long> {

    List<TagDomainService> findByTagDomain(TagDomain tagDomain);

    List<TagDomainService> findByService(Service service);

    TagDomainService findByServiceAndTagDomain(Service service, TagDomain tagDomain);
}
