package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Tag;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface TagRepository extends GraphRepository<Tag> {
    @Override
    @RestResource(exported = false)
    public Tag save(Tag entity);

    /*******************************************************************************
     ********************************************************************************/

    @Override
    @RestResource(exported = false)
    public Tag findOne(Long id);

    //@Query("MATCH (n) WHERE urn(n)={0} RETURN n")
    @RestResource(exported = false)
    //@Query("MATCH (node:Tag) WHERE node.urn = {0} RETURN node")
    Tag findByUrn(String Urn);

    @Override
    @RestResource(exported = false)
    public List<Tag> findAll();

    @Override
    @RestResource(exported = false)
    public long count();

    @Override
    @RestResource(exported = false)
    public void delete(Long id);


}
