package eu.oc.annotations.controller;

import eu.oc.annotations.domain.Application;
import eu.oc.annotations.domain.Service;
import eu.oc.annotations.domain.Tag;
import eu.oc.annotations.domain.TagDomain;
import eu.oc.annotations.handlers.RestException;
import eu.oc.annotations.repositories.*;
import eu.oc.annotations.service.AnnotationService;
import eu.oc.annotations.service.KPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TagDomainBrowser {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagDomainBrowser.class);

    @Autowired
    TagDomainRepository tagDomainRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    AnnotationService annotationService;

    @Autowired
    KPIService kpiService;

    // TAG DOMAIN METHODS-----------------------------------------------

    //Get All tagDomains
    @RequestMapping(value = {"tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomain> domainFindAll(Principal principal) {
        kpiService.addEvent(principal, "api:tagDomains");
        return tagDomainRepository.findAll();
    }

    //Get tagDomain
    @RequestMapping(value = {"tagDomains/{tagDomainUrn}"}, method = RequestMethod.GET)
    public final TagDomain domainFindByUrn(@PathVariable("tagDomainUrn") String tagDomainUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:tagDomain", "tagDomainUrn", tagDomainUrn);
        return tagDomainRepository.findByUrn(tagDomainUrn);
    }

    // TAG METHODS-----------------------------------------------

    @RequestMapping(value = {"tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.GET)
    public final List<Tag> domainGetTags(@PathVariable("tagDomainUrn") String tagDomainUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:tagDomainTags", "tagDomainUrn", tagDomainUrn);
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        return new ArrayList<>(d.getTags());
    }

    @RequestMapping(value = {"tags/{tagUrn}"}, method = RequestMethod.GET)
    public final Tag tags(@PathVariable("tagUrn") String tagUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:tag", "tagUrn", tagUrn);
        Tag t = tagRepository.findByUrn(tagUrn);
        if (t == null) {
            throw new RestException("Tag Not Found");
        }
        return t;
    }

    // Services METHODS-----------------------------------------------
    @RequestMapping(value = {"services"}, method = RequestMethod.GET)
    public final List<Service> services(Principal principal) {
        kpiService.addEvent(principal, "api:services");
        return serviceRepository.findAll();
    }

    @RequestMapping(value = {"services/{serviceUrn}"}, method = RequestMethod.GET)
    public final Service services(@PathVariable("serviceUrn") String serviceUrn, Principal principal) {
        kpiService.addEvent(principal, "api:service", "serviceUrn", serviceUrn);
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        return s;
    }

    @RequestMapping(value = {"services/{serviceUrn}/tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomain> serviceGetTagDomains(@PathVariable("serviceUrn") String serviceUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:serviceTagDomains", "serviceUrn", serviceUrn);
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        return tagDomainRepository.findAllByService(s.getUrn());
    }


    // Application Methods----------------------------------------------------------------------------------------------
    @RequestMapping(value = {"applications"}, method = RequestMethod.GET)
    public final List<Application> applications(Principal principal) {
        kpiService.addEvent(principal, "api:applications");
        return applicationRepository.findAll();
    }

    @RequestMapping(value = {"applications/{applicationUrn}"}, method = RequestMethod.GET)
    public final Application applications(@PathVariable("applicationUrn") String applicationUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:application", "applicationUrn", applicationUrn);
        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Service Not Found");
        }
        return a;
    }

    @RequestMapping(value = {"applications/{applicationUrn}/tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomain> applicationGetTagDomains(@PathVariable("applicationUrn") String applicationUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:applicationTagDomains", "applicationUrn", applicationUrn);
        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        return a.getTagDomains();
    }


}
