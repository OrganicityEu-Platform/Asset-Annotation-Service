package eu.oc.annotations.service;


import eu.oc.annotations.config.OrganicityAccount;
import eu.oc.annotations.domain.TagDomain;
import eu.oc.annotations.domain.TagDomainService;
import eu.oc.annotations.repositories.TagDomainServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    TagDomainServiceRepository tagDomainServiceRepository;

    public boolean canCreate(OrganicityAccount ou) {
        return ou.isAdministrator() || ou.isExperimenter();
    }

    public boolean canEdit(TagDomain d, OrganicityAccount ou) {
        List<TagDomainService> services = tagDomainServiceRepository.findByTagDomain(d);
        if (services != null) {
            if (services.isEmpty()) {
                LOGGER.info(ou.isExperimenter() + "|| " + ou.isAdministrator());
                return ou.isExperimenter() || ou.isAdministrator();
            } else {
                LOGGER.info("" + ou.isAdministrator());
                return ou.isAdministrator();
            }
        } else {
            LOGGER.info(ou.isExperimenter() + "|| " + ou.isAdministrator());
            return ou.isExperimenter() || ou.isAdministrator();
        }
    }

    public boolean canEdit(eu.oc.annotations.domain.Service s, OrganicityAccount ou) {
        return ou.isAdministrator();
    }

    public boolean canCreateService(OrganicityAccount ou) {
        return ou.isAdministrator();
    }
}