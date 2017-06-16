package eu.organicity.annotation.config;

import io.jsonwebtoken.Claims;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
    private Collection<Experiment> experiments;
    
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
                experiments = getExperiments(super.getKeycloakSecurityContext().getTokenString());
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
        if (!isExperimenter())
            return false;
        for (Experiment experiment : experiments) {
            if (experimentId.equals(experiment.getExperimentId())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isTheOnlyExperimnterUsingTagDomain(final Collection<eu.organicity.annotation.domain.Experiment> experiments) {
        if (experiments == null || experiments.isEmpty()) {
            return false;
        }
        for (eu.organicity.annotation.domain.Experiment app : experiments) {
            //            if (!ownsExperiment(app.getUrn())) {
            //                return false;
            //            }
        }
        return true;
    }
    
    public Set<String> getExperiments() {
        final HashSet<String> expIds = new HashSet<>();
        for (final Experiment experiment : experiments) {
            expIds.add(experiment.getExperimentId());
        }
        return expIds;
    }
    
    private String baseUrl = "https://experimenters.organicity.eu:8443/";
    
    public List<Experiment> getExperiments(String token) {
        
        RestTemplate template = new RestTemplate();
        try {
            LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Authorization", "Bearer " + token);
            ResponseEntity<ExperimentList> response = template.exchange(baseUrl + "/experiments", HttpMethod.GET, new HttpEntity(headers), ExperimentList.class);
            return response.getBody().getExperiments();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "OrganicityAccount{" + "id='" + id + '\'' + ", user='" + user + '\'' + ", expiration=" + expiration + ", email='" + email + '\'' + ", roles=" + roles + ", experiments=" + experiments + ", baseUrl='" + baseUrl + '\'' + '}';
    }
}
