package eu.oc.annotations.service;


import eu.oc.annotations.config.OrganicityAccount;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class OrganicityUserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganicityUserDetailsService.class);

    public static OrganicityAccount getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            OrganicityAccount oa = new OrganicityAccount((KeycloakPrincipal) authentication.getPrincipal(), authentication.getAuthorities());
            try {
                oa.parse();
            } catch (Exception e) {
                SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                SecurityContextHolder.clearContext();
                throw new AccessDeniedException(e.getMessage());
            }
            return oa;
        }
        return null;
    }
}