package eu.organicity.annotation.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperimentList {
    private List<Experiment> experiments;
    
    public ExperimentList() {
    }
    
    public List<Experiment> getExperiments() {
        return experiments;
    }
    
    public void setExperiments(List<Experiment> experiments) {
        this.experiments = experiments;
    }
}

