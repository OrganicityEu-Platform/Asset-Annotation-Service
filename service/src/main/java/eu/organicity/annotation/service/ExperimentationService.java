package eu.organicity.annotation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.organicity.annotation.domain.experiment.Experiment;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ExperimentationService {
    @Value("${oc.api.experimenters}")
    private String baseUrl;
    
    
    public Experiment[] getExperiments(String experimenter) {
        
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get(baseUrl + "/experiments-full/" + experimenter).header("accept", "application/json").header("Content-Type", "application/json").asObject(JsonNode.class);
            if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
                Experiment[] e = null;
                ObjectMapper mapper = new ObjectMapper();
                try {
                    e = (new Gson()).fromJson(jsonResponse.getBody().toString(), Experiment[].class);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return e;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        
        
        return null;
    }
    
    
}
