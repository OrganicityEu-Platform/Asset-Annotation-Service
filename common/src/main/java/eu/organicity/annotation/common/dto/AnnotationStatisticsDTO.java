package eu.organicity.annotation.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnotationStatisticsDTO {
    private String assetUrn;
    private long annotationsCount;
    private long firstAnnotation;
    private long lastAnnotation;
    private long globalAnnotationsCount;
    private long globalLastAnnotation;
    private long globalFirstAnnotation;
    
    public String getAssetUrn() {
        return assetUrn;
    }
    
    public void setAssetUrn(String assetUrn) {
        this.assetUrn = assetUrn;
    }
    
    public long getAnnotationsCount() {
        return annotationsCount;
    }
    
    public void setAnnotationsCount(long annotationsCount) {
        this.annotationsCount = annotationsCount;
    }
    
    public long getFirstAnnotation() {
        return firstAnnotation;
    }
    
    public void setFirstAnnotation(long firstAnnotation) {
        this.firstAnnotation = firstAnnotation;
    }
    
    public long getLastAnnotation() {
        return lastAnnotation;
    }
    
    public void setLastAnnotation(long lastAnnotation) {
        this.lastAnnotation = lastAnnotation;
    }
    
    public long getGlobalAnnotationsCount() {
        return globalAnnotationsCount;
    }
    
    public void setGlobalAnnotationsCount(long globalAnnotationsCount) {
        this.globalAnnotationsCount = globalAnnotationsCount;
    }
    
    public long getGlobalLastAnnotation() {
        return globalLastAnnotation;
    }
    
    public void setGlobalLastAnnotation(long globalLastAnnotation) {
        this.globalLastAnnotation = globalLastAnnotation;
    }
    
    public long getGlobalFirstAnnotation() {
        return globalFirstAnnotation;
    }
    
    public void setGlobalFirstAnnotation(long globalFirstAnnotation) {
        this.globalFirstAnnotation = globalFirstAnnotation;
    }
    
}
