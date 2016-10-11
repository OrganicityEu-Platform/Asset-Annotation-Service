package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Application;
import eu.oc.annotations.domain.TagDomain;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "application", path = "application")
public interface ApplicationRepository extends GraphRepository<Application> {
    @Override
    @RestResource(exported = false)
    Application save(Application entity);

    /*******************************************************************************
     ********************************************************************************/

    @Override
    @RestResource(exported = false)
    Application findOne(Long id);


    //@Query("MATCH (n) WHERE urn(n)={0} RETURN n")
    //@Query("MATCH (node:Asset) WHERE node.urn = {0} RETURN node limit 1")
    @RestResource(exported = false)
    Application findByUrn(String Urn);

    @RestResource(exported = false)
    List<Application> findAll();

    @Override
    @RestResource(exported = false)
    long count();

    @Override
    @RestResource(exported = false)
    void delete(Long id);


    @Query("MATCH (td:Application)-[:USES]->(t:TagDomain) WHERE t.urn = {0} RETURN td")
    @RestResource(exported = false)
    List<Application> findApplicationsUsingTagDomain(String Urn);


}
