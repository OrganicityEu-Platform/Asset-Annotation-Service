package eu.oc.annotations.config;

import eu.oc.annotations.domain.Application;
import eu.oc.annotations.domain.experiment.Experiment;
import eu.oc.annotations.repositories.ApplicationRepository;
import eu.oc.annotations.service.ExperimentationService;
import io.jsonwebtoken.Claims;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

/**
 * Created by etheodor on 29/1/2015.
 */
public final class OrganicityAccount extends KeycloakPrincipal {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganicityAccount.class);
    String id;
    String user;
    Date expiration;
    String email;
    Collection<? extends GrantedAuthority> roles;
    HashMap<String, Experiment> experiments = new HashMap<>();

    @Autowired
    ApplicationRepository applicationRepository;


    public OrganicityAccount(KeycloakPrincipal k, Collection<? extends GrantedAuthority> authorities) {
        super(k.getName(), k.getKeycloakSecurityContext());
        roles = authorities;
    }

    public void parse() throws Exception {
        try {
            JwtParser fwtparser = new JwtParser();
            Claims claims = fwtparser.parseJWT(super.getKeycloakSecurityContext().getTokenString());
            id = claims.getId();
            user = claims.getSubject();
            expiration = claims.getExpiration();
            email = (String) claims.get("email");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        if (isExperimenter()) {
            Experiment[] exps = ExperimentationService.getExperiments(this.getUser());
            for (Experiment e : exps) {
                experiments.put(e.getExperimentId(), e);
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getEmail() {
        return email;
    }

    public boolean isExperimenter() {
        for (GrantedAuthority role : roles) {
            if (role.getAuthority().equals("experimenter")) return true;
        }
        return false;
    }

    public boolean isAdministrator() {
        for (GrantedAuthority role : roles) {
            if (role.getAuthority().equals("administrator")) return true;
        }
        return false;
    }

    public boolean isParticipant(String experimentId) {
        if (experimentId == null)
            return false;
        else return true;
    }

    public boolean ownsExperiment(String experimentId) {
        if (isExperimenter() == false) return false;
        return experiments.containsKey(experimentId);
    }

    public boolean isTheOnlyExperimnterUsingTagDomain(final Collection<Application> applications) {
        if (applications == null || applications.isEmpty()) {
            return true;
        }
        for (Application app : applications) {
            if (!ownsExperiment(app.getUrn())) {
                return false;
            }
        }
        return true;
    }

    public Set<String> getExperiments() {
        return experiments.keySet();
    }

    @Override
    public String toString() {
        return "OrganicityAccount{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", expiration=" + expiration +
                ", email='" + email + '\'' +
                ", roles=" + (roles != null ? Arrays.toString(roles.toArray()) : "") +
                ", experiments='" + Arrays.toString(experiments.keySet().toArray()) + '\'' +
                '}';
    }
}
