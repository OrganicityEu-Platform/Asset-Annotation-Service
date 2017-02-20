package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Service;
import eu.oc.annotations.domain.TagDomain;
import eu.oc.annotations.domain.TagDomainService;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagDomainServiceRepository extends CrudRepository<TagDomainService, Long> {

    List<TagDomainService> findByTagDomain(TagDomain tagDomain);

    List<TagDomainService> findByService(Service service);

    TagDomainService findByServiceAndTagDomain(Service service, TagDomain tagDomain);
}
