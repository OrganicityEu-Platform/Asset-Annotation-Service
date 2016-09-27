package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.TagDomain;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface TagDomainRepository extends GraphRepository<TagDomain> {
    @Override
    @RestResource(exported = true)
    public TagDomain save(TagDomain entity);

    /*******************************************************************************
     ********************************************************************************/

    @Override
    @RestResource(exported = false)
    public TagDomain findOne(Long id);

    //@Query("MATCH (n) WHERE urn(n)={0} RETURN n")
    //@Query("MATCH (node:TagDomain) WHERE node.urn = {0} RETURN node")
    //@Query("MATCH p=(node:TagDomain)-[r*0..1]->() WHERE node.urn = {0} RETURN p")
    @RestResource(exported = false)
    TagDomain findByUrn(String Urn);


    @RestResource(exported = false)
    public List<TagDomain> findAll();

    @Override
    @RestResource(exported = false)
    public long count();

    @Override
    @RestResource(exported = false)
    public void delete(Long id);


    @RestResource(exported = false)
    @Query("MATCH (td:TagDomain)-[:LINK]->(s:Service) WHERE s.urn = {0} RETURN td")
    //@Query("MATCH p=(node:TagDomain)-[r*0..1]->() WHERE node.urn = {0} RETURN p")
    List<TagDomain> findAllByService(String serviceUrn);


    @RestResource(exported = false)
    @Query("MATCH (td:TagDomain)-[:HAS]->(t:Tag) WHERE t.urn = {0} RETURN td")
        //@Query("MATCH p=(node:TagDomain)-[r*0..1]->() WHERE node.urn = {0} RETURN p")
    TagDomain findByTag(String tagUrn);
}
