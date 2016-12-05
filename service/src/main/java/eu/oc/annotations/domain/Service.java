package eu.oc.annotations.domain;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import javax.validation.constraints.NotNull;

/**
 * Created by etheodor on 12/04/2016.
 */
@NodeEntity
public class Service {

    @GraphId
    private Long id;

    @Property(name = "urn")
    @NotNull
    private String urn;

    @Property(name = "description")
    private String description;

    @Property(name = "user")
    private String user;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

