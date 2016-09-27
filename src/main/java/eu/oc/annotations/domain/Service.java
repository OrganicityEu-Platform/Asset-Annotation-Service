package eu.oc.annotations.domain;

/**
 * Created by etheodor on 12/04/2016.
 */

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import javax.validation.constraints.NotNull;

@NodeEntity
public class Service {

    @GraphId
    private Long id;

    @Property(name = "urn")
    @NotNull
    private String urn;

    @Property(name = "description")
    private String description;

    public Service() {
    }

    public Service(String urn, String description) {
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

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", urn='" + urn + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

