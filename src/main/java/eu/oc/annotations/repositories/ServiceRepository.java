package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Service;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ServiceRepository extends GraphRepository<Service> {
    @Override
    @RestResource(exported = false)
    public Service save(Service entity);

    /*******************************************************************************
     ********************************************************************************/

    @Override
    @RestResource(exported = false)
    public Service findOne(Long id);

    //@Query("MATCH (n) WHERE urn(n)={0} RETURN n")
    @RestResource(exported = false)
    //@Query("MATCH (node:Service) WHERE node.urn = {0} RETURN node")
    Service findByUrn(String Urn);

    @Override
    @RestResource(exported = false)
    public List<Service> findAll();

    @Override
    @RestResource(exported = false)
    public long count();

    @Override
    @RestResource(exported = false)
    public void delete(Long id);
}
