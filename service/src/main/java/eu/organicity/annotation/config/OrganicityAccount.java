package eu.organicity.annotation.config;

import eu.organicity.annotation.domain.Experiment;
import eu.organicity.annotation.service.ExperimentationService;
import io.jsonwebtoken.Claims;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by etheodor on 29/1/2015.
 */
public final class OrganicityAccount extends KeycloakPrincipal {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganicityAccount.class);
    private String id;
    private String user;
    private Date expiration;
    private String email;
    private Collection<? extends GrantedAuthority> roles;
    private HashMap<String, eu.organicity.annotation.domain.experiment.Experiment> experiments = new HashMap<>();
    
    @Autowired
    ExperimentationService experimentationService;
    
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
            try {
                eu.organicity.annotation.domain.experiment.Experiment[] exps = experimentationService.getExperiments(this.getUser());
                for (eu.organicity.annotation.domain.experiment.Experiment e : exps) {
                    experiments.put(e.getExperimentId(), e);
                }
            } catch (Exception e) {
                LOGGER.error(e.getLocalizedMessage(), e);
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
            if (role.getAuthority().equals("experimenter"))
                return true;
        }
        return false;
    }
    
    public boolean isAdministrator() {
        for (GrantedAuthority role : roles) {
            if (role.getAuthority().equals("administrator"))
                return true;
        }
        return false;
    }
    
    public boolean isParticipant(String experimentId) {
        if (experimentId == null)
            return false;
        else
            return true;
    }
    
    public boolean ownsExperiment(String experimentId) {
        if (isExperimenter() == false)
            return false;
        return experiments.containsKey(experimentId);
    }
    
    public boolean isTheOnlyExperimnterUsingTagDomain(final Collection<Experiment> experiments) {
        if (experiments == null || experiments.isEmpty()) {
            return false;
        }
        for (Experiment app : experiments) {
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
        return "OrganicityAccount{" + "id='" + id + '\'' + ", user='" + user + '\'' + ", expiration=" + expiration + ", email='" + email + '\'' + ", roles=" + (roles != null ? Arrays.toString(roles.toArray()) : "") + ", experiments='" + Arrays.toString(experiments.keySet().toArray()) + '\'' + '}';
    }
}
