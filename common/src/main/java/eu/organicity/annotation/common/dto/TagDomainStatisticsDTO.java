package eu.organicity.annotation.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDomainStatisticsDTO {
    private Long id;
    private String urn;
    private Map<String, Long> counts;
    private Set<AnnotationDTO> annotations;
    
    public TagDomainStatisticsDTO() {
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
    
    public Map<String, Long> getCounts() {
        return counts;
    }
    
    public void setCounts(Map<String, Long> counts) {
        this.counts = counts;
    }
    
    public Set<AnnotationDTO> getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(Set<AnnotationDTO> annotations) {
        this.annotations = annotations;
    }
}
