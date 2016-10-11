package eu.oc.annotations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.oc.annotations.domain.experiment.Experiment;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;


public class ExperimentationService {
    final static String url = "http://experimenters.organicity.eu:8081/experiments-full/";


    public static Experiment[] getExperiments(String experimenter) {

        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get(url + experimenter)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .asObject(JsonNode.class);
            if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
                Experiment[] e=null;
                ObjectMapper mapper = new ObjectMapper();
                try {
                    e= (new Gson()).fromJson(jsonResponse.getBody().toString(), Experiment[].class);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return  e;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        return null;
    }


}
