package eu.oc.annotations.service;


import eu.oc.annotations.config.OrganicityAccount;
import eu.oc.annotations.domain.TagDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);


    public boolean canEdit(TagDomain d, OrganicityAccount ou) {
        if (d.getServices() != null && !d.getServices().isEmpty()) {
            if (ou.isAdministrator()) {
                return true;
            }


        } else {
            if (ou.isAdministrator() || ou.isExperimenter()) {
                return true;
            }
        }
        return false;
    }

    public boolean canEdit(eu.oc.annotations.domain.Service s, OrganicityAccount ou) {
        if (ou.isAdministrator()) {
            return true;
        }
        return false;
    }
}