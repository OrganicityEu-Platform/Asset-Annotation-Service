package eu.organicity.annotation.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String urn;

    private String name;

    private String user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tagDomainId")
    private TagDomain tagDomain;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public TagDomain getTagDomain() {
        return tagDomain;
    }

    public void setTagDomain(TagDomain tagDomain) {
        this.tagDomain = tagDomain;
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
