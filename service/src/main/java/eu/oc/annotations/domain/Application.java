package eu.oc.annotations.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by etheodor on 12/04/2016.
 */
@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String urn;

    private String description;

    @OneToMany(mappedBy = "application")
    private List<TagDomain> tagDomains = new ArrayList<>();

    private String user;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

