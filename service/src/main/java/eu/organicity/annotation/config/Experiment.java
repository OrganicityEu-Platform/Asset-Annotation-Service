package eu.organicity.annotation.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Experiment{
    private String experimentId;
    
    public Experiment() {
    }
    
    public String getExperimentId() {
        return experimentId;
    }
    
    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }
}
