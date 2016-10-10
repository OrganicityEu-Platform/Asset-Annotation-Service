package eu.oc.annotations.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import eu.oc.annotations.config.OrganicityAccount;
import eu.oc.annotations.domain.experiment.Experiment;
import eu.oc.annotations.service.ExperimentationService;
import eu.oc.annotations.service.OrganicityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    ExperimentationService experimentationService;

    @RequestMapping(value = {"/", "token"}, method = RequestMethod.GET)
    public final String token() {

        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        return ou.toString();
    }

    @RequestMapping(value = {"experiments"}, method = RequestMethod.GET)
    public final Experiment[] experiments() throws UnirestException {

        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (ou.isExperimenter()) {
            return experimentationService.getExperiment(ou.getUser());
        }
        return new Experiment[0];
    }

}
