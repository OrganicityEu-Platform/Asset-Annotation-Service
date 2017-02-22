package eu.organicity.annotation.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentDTO {
    
    private Long id;
    private String urn;
    private String description;
    private String user;
    private Long created;
    private Long modified;
    private List<TagDomainDTO> tagDomains;
    
    
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
    
    public List<TagDomainDTO> getTagDomains() {
        return tagDomains;
    }
    
    public void setTagDomains(List<TagDomainDTO> tagDomains) {
        this.tagDomains = tagDomains;
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
        return "ExperimentDTO{" + "urn='" + urn + '\'' + '}';
    }
}
