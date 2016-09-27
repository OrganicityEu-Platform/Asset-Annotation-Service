package eu.oc.annotations.repositories;

import eu.oc.annotations.domain.Asset;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "asset", path = "asset")
public interface AssetRepository extends GraphRepository<Asset> {
    @Override
    @RestResource(exported = false)
    public Asset save(Asset entity);

    /*******************************************************************************
     ********************************************************************************/

    @Override
    @RestResource(exported = false)
    public Asset findOne(Long id);



    @Query("MATCH (node:Asset)-[tg:TAGGING]-(t:Tag) WHERE node.urn = {0} RETURN node,tg")
    @RestResource(exported = false)
    Asset findByUrn(String urn);

 /*   @Override
    @RestResource(exported = false)
    public Page<Asset> findAll(Pageable pageable);*/

    @Override
    @RestResource(exported = false)
    public long count();

    @Override
    @RestResource(exported = false)
    public void delete(Long id);
}
