package eu.oc.annotations.config;

import io.jsonwebtoken.Claims;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

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

    public OrganicityAccount(KeycloakPrincipal k, Collection<? extends GrantedAuthority> authorities) {
        super(k.getName(), k.getKeycloakSecurityContext());
        roles = authorities;
    }

    public void parse() throws Exception {
        try {
            JwtParser fwtparser = new JwtParser();
            Claims claims = fwtparser.parseJWT(super.getKeycloakSecurityContext().getIdTokenString());
            id = claims.getId();
            user = claims.getSubject();
            expiration = claims.getExpiration();
            email = (String) claims.get("email");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
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

    public boolean isParticipant() {
        for (GrantedAuthority role : roles) {
            if (role.getAuthority().equals("participant")) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "OrganicityAccount{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", expiration=" + expiration +
                ", email='" + email + '\'' +
                ", roles=" + (roles != null ? Arrays.toString(roles.toArray()) : "") +
                '}';
    }
}
