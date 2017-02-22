package eu.organicity.annotation.domain;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

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
    
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<Tagging> taggings;
    
    @CreatedDate
    private ZonedDateTime createdDate = ZonedDateTime.now();
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate;
    
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
    
    public List<Tagging> getTaggings() {
        return taggings;
    }
    
    public void setTaggings(List<Tagging> taggings) {
        this.taggings = taggings;
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
        return "Tag{" + "id=" + id + ", urn='" + urn + '\'' + ", name='" + name + '\'' + '}';
    }
}
