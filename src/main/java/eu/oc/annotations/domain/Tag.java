package eu.oc.annotations.domain;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import javax.validation.constraints.NotNull;

@NodeEntity
public class Tag {

    @GraphId
    private Long id;

    @Property(name = "urn")
    @NotNull
    private String urn;

    @Property(name = "name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", urn='" + urn + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
