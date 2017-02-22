package eu.organicity.annotation.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tagDomain")
public class TagDomain {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    @Column(unique = true)
    private String urn;
    
    private String description;
    
    private String user;
    
    @OneToMany(mappedBy = "tagDomain", fetch = FetchType.EAGER)
    private List<Tag> tags;
    
    @CreatedDate
    private ZonedDateTime createdDate = ZonedDateTime.now();
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate;
    
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
        if (tags == null)
            tags = new ArrayList<>();
        return tags;
    }
    
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    @Override
    public String toString() {
        return "TagDomain{" + "id=" + id + ", urn='" + urn + '\'' + ", description='" + description + '\'' + ", tags=" + tags + '}';
    }
    
    public Boolean containsTag(String tagUrn) {
        for (Tag t : tags) {
            if (t.getUrn().equals(tagUrn))
                return true;
        }
        return false;
    }
}
