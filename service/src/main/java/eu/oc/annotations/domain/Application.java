package eu.oc.annotations.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by etheodor on 12/04/2016.
 */
@NodeEntity
public class Application {

    @GraphId
    private Long id;

    @Property(name = "urn")
    @NotNull
    private String urn;

    @Property(name = "description")
    private String description;

    @Relationship(type = "USES", direction = "UNDIRECTED")
    private List<TagDomain> tagDomains=new ArrayList<>();

    public Application() {
    }

    public Application(String urn, String description) {
        this.urn = urn;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TagDomain> getTagDomains() {
        return tagDomains;
    }

    public void setTagDomains(List<TagDomain> tagDomains) {
        this.tagDomains = tagDomains;
    }


    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", urn='" + urn + '\'' +
                ", description='" + description + '\'' +
                ", tagDomains=" + Arrays.toString(tagDomains.toArray()) +
                '}';
    }
}

