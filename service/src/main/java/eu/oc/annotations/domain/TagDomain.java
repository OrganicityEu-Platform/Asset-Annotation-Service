package eu.oc.annotations.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class TagDomain {

    @GraphId
    private Long id;

    @Property(name = "urn")
    @NotNull
    private String urn;

    @Property(name = "description")
    private String description;

    @Property(name = "user")
    private String user;


    @Relationship(type = "HAS", direction = "UNDIRECTED")
    private List<Tag> tags;

    @Relationship(type = "LINK", direction = "UNDIRECTED")
    private List<Service> services;


    public TagDomain() {
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

    public List<Tag> getTags() {
        if (tags == null) tags = new ArrayList<>();
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Service> getServices() {
        if (services == null) services = new ArrayList<>();
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TagDomain{" +
                "id=" + id +
                ", urn='" + urn + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", services=" + services +
                '}';
    }

    public Boolean containsTag(String tagUrn) {
        for (Tag t : tags) {
            if (t.getUrn().equals(tagUrn)) return true;
        }
        return false;
    }
}
