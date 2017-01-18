package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Tag;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Set;

@RepositoryRestResource(exported = false)
public interface TagRepository extends GraphRepository<Tag> {
    @Override
    @RestResource(exported = false)
    Tag save(Tag entity);

    /*******************************************************************************
     ********************************************************************************/

    @Override
    @RestResource(exported = false)
    Tag findOne(Long id);

    //@Query("MATCH (n) WHERE urn(n)={0} RETURN n")
    @RestResource(exported = false)
    //@Query("MATCH (node:Tag) WHERE node.urn = {0} RETURN node")
    Tag findByUrn(String Urn);

    @Query("MATCH (node:Tag) WHERE node.urn = {0} RETURN node")
    Set<Tag> findAllByUrn(String Urn);

    @Override
    @RestResource(exported = false)
    List<Tag> findAll();

    @Override
    @RestResource(exported = false)
    long count();

    @Override
    @RestResource(exported = false)
    void delete(Long id);


}
