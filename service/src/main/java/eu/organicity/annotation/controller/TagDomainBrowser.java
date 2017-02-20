package eu.organicity.annotation.controller;

import eu.organicity.annotation.domain.Application;
import eu.organicity.annotation.domain.Service;
import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.handlers.RestException;
import eu.organicity.annotation.service.AnnotationService;
import eu.organicity.annotation.service.DTOService;
import eu.organicity.annotation.service.KPIService;
import eu.organicity.annotation.common.dto.ExperimentDTO;
import eu.organicity.annotation.common.dto.ServiceDTO;
import eu.organicity.annotation.common.dto.TagDTO;
import eu.organicity.annotation.common.dto.TagDomainDTO;
import eu.organicity.annotation.repositories.ApplicationRepository;
import eu.organicity.annotation.repositories.ServiceRepository;
import eu.organicity.annotation.repositories.TagDomainRepository;
import eu.organicity.annotation.repositories.TagDomainServiceRepository;
import eu.organicity.annotation.repositories.TagRepository;
import eu.organicity.annotation.repositories.TaggingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    TaggingRepository taggingRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    TagDomainServiceRepository tagDomainServiceRepository;

    @Autowired
    AnnotationService annotationService;

    @Autowired
    KPIService kpiService;

    @Autowired
    DTOService dtoService;

    // TAG DOMAIN METHODS-----------------------------------------------

    //Get All tagDomains
    @RequestMapping(value = {"tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomainDTO> domainFindAll(Principal principal) {
        kpiService.addEvent(principal, "api:tagDomains");
        return dtoService.toTagDomainListDTO(tagDomainRepository.findAll());
    }


    //Get tagDomain
    @RequestMapping(value = {"tagDomains/{tagDomainUrn}"}, method = RequestMethod.GET)
    public final TagDomainDTO domainFindByUrn(@PathVariable("tagDomainUrn") String tagDomainUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:tagDomain", "tagDomainUrn", tagDomainUrn);
        return dtoService.toDTO(tagDomainRepository.findByUrn(tagDomainUrn));
    }

    // TAG METHODS-----------------------------------------------

    @RequestMapping(value = {"tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.GET)
    public final Set<TagDTO> domainGetTags(@PathVariable("tagDomainUrn") String tagDomainUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:tagDomainTags", "tagDomainUrn", tagDomainUrn);
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        try {
            return dtoService.toDTO(d).getTags();
        } catch (NullPointerException npe) {
            LOGGER.error(npe.getMessage(), npe);
            return new HashSet<>();
        }
    }

    @RequestMapping(value = {"tags/{tagUrn}"}, method = RequestMethod.GET)
    public final TagDTO tags(@PathVariable("tagUrn") String tagUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:tag", "tagUrn", tagUrn);
        Tag t = tagRepository.findByUrn(tagUrn);
        if (t == null) {
            throw new RestException("Tag Not Found");
        }
        return dtoService.toDTO(t);
    }

    // Services METHODS-----------------------------------------------
    @RequestMapping(value = {"services"}, method = RequestMethod.GET)
    public final List<ServiceDTO> services(Principal principal) {
        kpiService.addEvent(principal, "api:services");
        return dtoService.toServiceListDTO(tagDomainServiceRepository.findAll());
    }

    @RequestMapping(value = {"services/{serviceUrn}"}, method = RequestMethod.GET)
    public final ServiceDTO services(@PathVariable("serviceUrn") String serviceUrn, Principal principal) {
        kpiService.addEvent(principal, "api:service", "serviceUrn", serviceUrn);
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        return dtoService.toDTO(s);
    }

    @RequestMapping(value = {"services/{serviceUrn}/tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomainDTO> serviceGetTagDomains(@PathVariable("serviceUrn") String serviceUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:serviceTagDomains", "serviceUrn", serviceUrn);
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        return dtoService.toTagDomainListDTOFromTagDomainServices(tagDomainServiceRepository.findByService(s));
    }


    // Application Methods----------------------------------------------------------------------------------------------
    @RequestMapping(value = {"experiments"}, method = RequestMethod.GET)
    public final List<ExperimentDTO> experiments(Principal principal) {
        kpiService.addEvent(principal, "api:experiments");
        return dtoService.toExperimentListDTO(applicationRepository.findAll());
    }

    @RequestMapping(value = {"experiments/{experimentUrn}"}, method = RequestMethod.GET)
    public final ExperimentDTO experiments(@PathVariable("experimentUrn") String experimentUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:experiments", "experimentUrn", experimentUrn);
        Application a = applicationRepository.findByUrn(experimentUrn);
        if (a == null) {
            throw new RestException("Experiment Not Found");
        }
        return dtoService.toDTO(a);
    }

    @RequestMapping(value = {"experiments/{experimentUrn}/tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomainDTO> experimentGetTagDomains(@PathVariable("experimentUrn") String experimentUrn
            , Principal principal) {
        kpiService.addEvent(principal, "api:experimentTagDomains", "experimentUrn", experimentUrn);
        Application a = applicationRepository.findByUrn(experimentUrn);
        if (a == null) {
            throw new RestException("Experiment Not Found");
        }
        return dtoService.toTagDomainListDTO(a.getTagDomains());
    }


}
