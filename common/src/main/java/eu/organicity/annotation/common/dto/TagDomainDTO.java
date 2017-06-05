package eu.organicity.annotation.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDomainDTO {
    private Long id;
    private String urn;
    private String description;
    private String user;
    private Set<TagDTO> tags;
    private Set<ServiceDTO> services;
    private Long created;
    private Long modified;
    
    public TagDomainDTO() {
    }
    
    public TagDomainDTO(Long id, String urn, String description, Set<TagDTO> tags) {
        this.id = id;
        this.urn = urn;
        this.description = description;
        this.tags = tags;
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
    
    public Set<TagDTO> getTags() {
        return tags;
    }
    
    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }
    
    public Set<ServiceDTO> getServices() {
        return services;
    }
    
    public void setServices(Set<ServiceDTO> services) {
        this.services = services;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public Long getCreated() {
        return created;
    }
    
    public void setCreated(Long created) {
        this.created = created;
    }
    
    public Long getModified() {
        return modified;
    }
    
    public void setModified(Long modified) {
        this.modified = modified;
    }
    
    @Override
    public String toString() {
        return "TagDomainDTO{" +
                "id=" + id +
                ", urn='" + urn + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                '}';
    }
}
