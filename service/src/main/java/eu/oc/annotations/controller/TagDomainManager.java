package eu.oc.annotations.controller;

import eu.oc.annotations.config.OrganicityAccount;
import eu.oc.annotations.domain.Application;
import eu.oc.annotations.domain.Service;
import eu.oc.annotations.domain.Tag;
import eu.oc.annotations.domain.TagDomain;
import eu.oc.annotations.handlers.RestException;
import eu.oc.annotations.repositories.ApplicationRepository;
import eu.oc.annotations.repositories.ServiceRepository;
import eu.oc.annotations.repositories.TagDomainRepository;
import eu.oc.annotations.repositories.TagRepository;
import eu.oc.annotations.service.KPIService;
import eu.oc.annotations.service.OrganicityUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class TagDomainManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganicityAccount.class);

    @Autowired
    TagDomainRepository tagDomainRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    KPIService kpiService;

    // TAG DOMAIN METHODS--------------------------------------------------------------------------------

    //Create tagDomain
    @RequestMapping(value = {"admin/tagDomains"}, method = RequestMethod.POST)
    public final TagDomain domainCreate(@RequestBody TagDomain domain, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains", "tagDomainUrn", domain.getUrn());

        LOGGER.info("POST domainCreate");

        if (domain.getId() != null) {
            LOGGER.error("TagDomain Exception: TagDomain.id has to be null");
            throw new RestException("TagDomain Exception: TagDomain.id has to be null");
        }
        TagDomain a = tagDomainRepository.findByUrn(domain.getUrn());
        if (a != null) { //tagDomain Create
            LOGGER.error("TagDomain Exception: duplicate urn");
            throw new RestException("TagDomain Exception: duplicate urn");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        domain.setId(null);
        try {
            domain = tagDomainRepository.save(domain);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RestException(e.getMessage());
        }
        return domain;
    }

    //Update tagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}"}, method = RequestMethod.POST)
    public final TagDomain domainUpdate(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody TagDomain domain
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/update", "tagDomainUrn", domain.getUrn());

        LOGGER.info("POST domainUpdate");

        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            LOGGER.error("TagDomain Not Found");
            throw new RestException("TagDomain Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.isTheOnlyExperimnterUsingTagDomain(applicationRepository.findApplicationsUsingTagDomain(tagDomainUrn))) {
            LOGGER.error("TagDomain is used also from other experiments. Not possible to delete/update");
            throw new RestException("TagDomain is used also from other experiments. Not possible to delete/update");
        }
        d.setDescription(domain.getDescription());
        try {
            domain = tagDomainRepository.save(d);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RestException(e.getMessage());
        }
        return domain;
    }

    //Delete tagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}"}, method = RequestMethod.DELETE)
    public final void domainDelete(@PathVariable("tagDomainUrn") String tagDomainUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/delete", "tagDomainUrn", tagDomainUrn);

        LOGGER.info("DELETE domainDelete");

        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            LOGGER.error("TagDomain Not Found");
            throw new RestException("TagDomain Not Found");
        }
        if (d.getTags() != null && d.getTags().size() > 0) {
            LOGGER.error("TagDomain is not empty");
            throw new RestException("TagDomain is not empty");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.isTheOnlyExperimnterUsingTagDomain(applicationRepository.findApplicationsUsingTagDomain(tagDomainUrn))) {
            LOGGER.error("TagDomain is used also from other experiments. Not possible to delete/update");
            throw new RestException("TagDomain is used also from other experiments. Not possible to delete/update");
        }
        try {
            tagDomainRepository.delete(d);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }


    // TAG METHODS-----------------------------------------------------------------------------------------------------

    //Add tag to domain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.POST)
    public final Tag domainCreateTag(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody Tag tag
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/tags/add"
                , "tagDomainUrn", tagDomainUrn
                , "tagUrn", tag.getUrn()
        );

        LOGGER.info("POST domainCreateTag");

        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            LOGGER.error("TagDomain Not Found");
            throw new RestException("TagDomain Not Found");
        }
        Tag a = tagRepository.findByUrn(tag.getUrn());
        if (a != null) {
            LOGGER.error("Tag:Duplicate Urn");
            throw new RestException("Tag:Duplicate Urn");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.isTheOnlyExperimnterUsingTagDomain(applicationRepository.findApplicationsUsingTagDomain(tagDomainUrn))) {
            LOGGER.error("TagDomain is used also from other experiments. Not possible to delete/update");
            throw new RestException("TagDomain is used also from other experiments. Not possible to delete/update");
        }

        tag.setId(null);
        try {
            tag = tagRepository.save(tag);
            d.getTags().add(tag);
            tagDomainRepository.save(d);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RestException(e.getMessage());
        }
        return tag;
    }

    //delete Tag from TagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.DELETE)
    public final void domainRemoveTag(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody String tagUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/tags/delete"
                , "tagDomainUrn", tagDomainUrn
                , "tagUrn", tagUrn
        );

        LOGGER.info("DELETE domainRemoveTag " + tagUrn);

        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        tagUrn = tagUrn.replace("\"", "");
        Tag t = tagRepository.findByUrn(tagUrn);
        if (t == null) {
            throw new RestException("Tag Not Found");
        }
        if (!d.containsTag(tagUrn)) {
            throw new RestException("TagDomain/Tag are not associated");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.isTheOnlyExperimnterUsingTagDomain(applicationRepository.findApplicationsUsingTagDomain(tagDomainUrn))) {
            throw new RestException("TagDomain is used also from other experiments. Not possible to delete/update");
        }
        try {
            tagRepository.delete(t.getId());
            TagDomain domain = tagDomainRepository.findByUrn(tagDomainUrn);
            domain.getTags().remove(t);
            tagDomainRepository.save(domain);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }

    //get Services using this TagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.GET)
    public final List<Service> tagDomainGetServices(@PathVariable("tagDomainUrn") String tagDomainUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/services", "tagDomainUrn", tagDomainUrn);

        LOGGER.info("GET tagDomainGetServices");

        TagDomain a = tagDomainRepository.findByUrn(tagDomainUrn);
        if (a == null) {
            throw new RestException("TagDomain Not Found");
        }
        return a.getServices();
    }

    //Associate TagDomain with Service
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.POST)
    public final TagDomain serviceAddTagDomains(@RequestParam(value = "serviceUrn", required = true) String serviceUrn, @PathVariable("tagDomainUrn") String tagDomainUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/services/add"
                , "tagDomainUrn", tagDomainUrn
                , "serviceUrn", serviceUrn
        );

        LOGGER.info("POST serviceAddTagDomains");

        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        TagDomain a = tagDomainRepository.findByUrn(tagDomainUrn);
        if (a == null) {
            throw new RestException("TagDomain Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        try {
            a.getServices().add(s);
            return tagDomainRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }

    //Disassociate TagDomain with Service
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.DELETE)
    public final void serviceRemoveTagDomains(@RequestParam(value = "serviceUrn", required = true) String serviceUrn, @PathVariable("tagDomainUrn") String tagDomainUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/services/remove"
                , "tagDomainUrn", tagDomainUrn
                , "serviceUrn", serviceUrn
        );

        LOGGER.info("DELETE serviceRemoveTagDomains");

        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        TagDomain a = tagDomainRepository.findByUrn(tagDomainUrn);
        if (a == null) {
            throw new RestException("TagDomain Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        try {
            a.getServices().remove(s);
            tagDomainRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }


    // Services METHODS-----------------------------------------------

    //Create Service
    @RequestMapping(value = {"admin/services"}, method = RequestMethod.POST)
    public final Service servicesCreate(@RequestBody Service service, Principal principal) {
        kpiService.addEvent(principal, "api:admin/services/create", "serviceUrn", service.getUrn());

        LOGGER.info("POST servicesCreate");

        if (service.getId() != null) {
            throw new RestException("Service Exception: Service.id has to be null");
        }
        Service a = serviceRepository.findByUrn(service.getUrn());
        if (a != null) { //tagDomain Create
            throw new RestException("Service Exception: duplicate urn");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        service.setId(null);
        try {
            service = serviceRepository.save(service);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return service;
    }

    //Delete Service
    @RequestMapping(value = {"admin/services/{serviceUrn}"}, method = RequestMethod.DELETE)
    public final void serviceDelete(@PathVariable("serviceUrn") String serviceUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/services/delete", "serviceUrn", serviceUrn);

        LOGGER.info("DELETE serviceDelete");

        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        try {
            serviceRepository.delete(s);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }


    // Application METHODS-----------------------------------------------

    //Create Experiment
    @RequestMapping(value = {"admin/applications"}, method = RequestMethod.POST)
    public final Application applicationsCreate(@RequestBody Application application
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/applications/create", "applicationUrn", application.getUrn());

        LOGGER.info("POST applicationsCreate");

        if (application.getId() != null) {
            throw new RestException("Application Exception: Application.id has to be null");
        }
        Application a = applicationRepository.findByUrn(application.getUrn());
        if (a != null) { //tagDomain Create
            throw new RestException("Application Exception: duplicate urn");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        application.setId(null);
        try {
            application = applicationRepository.save(application);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return application;
    }

    //Delete Application
    @RequestMapping(value = {"admin/applications/{applicationUrn}"}, method = RequestMethod.DELETE)
    public final void applicationDelete(@PathVariable("applicationUrn") String applicationUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/applications/delete", "applicationUrn", applicationUrn);

        LOGGER.info("DELETE applicationDelete");

        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.ownsExperiment(applicationUrn)) {
            throw new RestException("Experimenter is not owning experiment");
        }
        //todo extra checks (e.g. existing taggings etc)
        try {
            applicationRepository.delete(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }

    //Show all available tag domains for application/experiment
    @RequestMapping(value = {"admin/applications/{applicationUrn}/tagDomains"}, method = RequestMethod.GET) //todo
    public final List<TagDomain> applicationGetTagDomains(@PathVariable("applicationUrn") String applicationUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/applications/tagDomains", "applicationUrn", applicationUrn);

        LOGGER.info("GET applicationGetTagDomains");

        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.ownsExperiment(applicationUrn)) {
            throw new RestException("Experimenter is not owning experiment");
        }
        return a.getTagDomains();
    }


    @RequestMapping(value = {"admin/applications/{applicationUrn}/tagDomains"}, method = RequestMethod.POST)
    public final Application applicationAddTagDomains(@RequestParam(value = "tagDomainUrn", required = true) String tagDomainUrn, @PathVariable("applicationUrn") String applicationUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/applications/tagDomains/add"
                , "applicationUrn", applicationUrn
                , "tagDomainUrn", tagDomainUrn
        );

        LOGGER.info("POST applicationAddTagDomains");

        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        TagDomain td = tagDomainRepository.findByUrn(tagDomainUrn);
        if (td == null) {
            throw new RestException("TagDomain Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.ownsExperiment(applicationUrn)) {
            throw new RestException("Experimenter is not owning experiment");
        }

        try {
            a.getTagDomains().add(td);
            a = applicationRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return a;
    }

    @RequestMapping(value = {"admin/applications/{applicationUrn}/tagDomains"}, method = RequestMethod.DELETE)
    public final void applicationRemoveTagDomains(@RequestParam(value = "tagDomainUrn", required = true) String tagDomainUrn, @PathVariable("applicationUrn") String applicationUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:admin/applications/tagDomains/remove"
                , "applicationUrn", applicationUrn
                , "tagDomainUrn", tagDomainUrn
        );

        LOGGER.info("DELETE applicationRemoveTagDomains");

        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        TagDomain td = tagDomainRepository.findByUrn(tagDomainUrn);
        if (td == null) {
            throw new RestException("TagDomain Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!ou.isAdministrator() && !ou.isExperimenter()) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.ownsExperiment(applicationUrn)) {
            throw new RestException("Experimenter is not owning experiment");
        }
        try {
            a.getTagDomains().remove(td);
            applicationRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }


}
