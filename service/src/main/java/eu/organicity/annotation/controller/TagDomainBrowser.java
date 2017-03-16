package eu.organicity.annotation.controller;

import eu.organicity.annotation.common.dto.AnnotationDTO;
import eu.organicity.annotation.common.dto.ExperimentDTO;
import eu.organicity.annotation.common.dto.ServiceDTO;
import eu.organicity.annotation.common.dto.TagDTO;
import eu.organicity.annotation.common.dto.TagDomainDTO;
import eu.organicity.annotation.common.exception.NotFoundException;
import eu.organicity.annotation.domain.Experiment;
import eu.organicity.annotation.domain.ExperimentTagDomain;
import eu.organicity.annotation.domain.Service;
import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.domain.Tagging;
import eu.organicity.annotation.repositories.ExperimentRepository;
import eu.organicity.annotation.repositories.ExperimentTagDomainRepository;
import eu.organicity.annotation.repositories.ServiceRepository;
import eu.organicity.annotation.repositories.TagDomainRepository;
import eu.organicity.annotation.repositories.TagDomainServiceRepository;
import eu.organicity.annotation.repositories.TagRepository;
import eu.organicity.annotation.repositories.TaggingRepository;
import eu.organicity.annotation.service.AccountingService;
import eu.organicity.annotation.service.AnnotationService;
import eu.organicity.annotation.service.DTOService;
import eu.organicity.annotation.service.KPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static eu.organicity.annotation.service.AccountingService.READ_ACTION;

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
    ExperimentRepository experimentRepository;

    @Autowired
    TagDomainServiceRepository tagDomainServiceRepository;

    @Autowired
    ExperimentTagDomainRepository experimentTagDomainRepository;

    @Autowired
    AnnotationService annotationService;

    @Autowired
    KPIService kpiService;

    @Autowired
    AccountingService accountingService;

    @Autowired
    DTOService dtoService;

    // TAG DOMAIN METHODS-----------------------------------------------

    //Get All tagDomains
    @RequestMapping(value = {"tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomainDTO> domainFindAll(Principal principal) {
        kpiService.addEvent(principal, "api:tagDomains");
        accountingService.addMethod(principal, READ_ACTION, "tagDomains");
        return dtoService.toTagDomainListDTO(tagDomainRepository.findAll());
    }


    //Get tagDomain
    @RequestMapping(value = {"tagDomains/{tagDomainUrn}"}, method = RequestMethod.GET)
    public final TagDomainDTO domainFindByUrn(@PathVariable("tagDomainUrn") String tagDomainUrn, Principal principal) {
        kpiService.addEvent(principal, "api:tagDomain", "tagDomainUrn", tagDomainUrn);
        accountingService.addMethod(principal, READ_ACTION, "domainFindByUrn", tagDomainUrn, null);
        return dtoService.toDTO(tagDomainRepository.findByUrn(tagDomainUrn));
    }

    // TAG METHODS-----------------------------------------------

    @RequestMapping(value = {"tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.GET)
    public final Set<TagDTO> domainGetTags(@PathVariable("tagDomainUrn") String tagDomainUrn, Principal principal) {
        kpiService.addEvent(principal, "api:tagDomainTags", "tagDomainUrn", tagDomainUrn);
        accountingService.addMethod(principal, READ_ACTION, "tagDomainTags", tagDomainUrn, null);
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        try {
            return dtoService.toDTO(d).getTags();
        } catch (NullPointerException npe) {
            LOGGER.error(npe.getMessage(), npe);
            return new HashSet<>();
        }
    }

    @RequestMapping(value = {"tags/{tagUrn}"}, method = RequestMethod.GET)
    public final TagDTO tags(@PathVariable("tagUrn") String tagUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:tag", "tagUrn", tagUrn);
        accountingService.addMethod(principal, READ_ACTION, "tag", tagUrn, null);
        Tag t = tagRepository.findByUrn(tagUrn);
        if (t == null) {
            throw new NotFoundException("Tag Not Found");
        }
        return dtoService.toDTO(t);
    }

    // Services METHODS-----------------------------------------------
    @RequestMapping(value = {"services"}, method = RequestMethod.GET)
    public final List<ServiceDTO> services(Principal principal) {
        kpiService.addEvent(principal, "api:services");
        accountingService.addMethod(principal, READ_ACTION, "services");
        return dtoService.toOriginalServiceListDTO(serviceRepository.findAll());
    }

    @RequestMapping(value = {"services/{serviceUrn}"}, method = RequestMethod.GET)
    public final ServiceDTO services(@PathVariable("serviceUrn") String serviceUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:service", "serviceUrn", serviceUrn);
        accountingService.addMethod(principal, READ_ACTION, "service", serviceUrn, null);
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new NotFoundException("Service Not Found");
        }
        return dtoService.toDTO(s);
    }

    @RequestMapping(value = {"services/{serviceUrn}/tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomainDTO> serviceGetTagDomains(@PathVariable("serviceUrn") String serviceUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:serviceTagDomains", "serviceUrn", serviceUrn);
        accountingService.addMethod(principal, READ_ACTION, "serviceTagDomains", serviceUrn, null);
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new NotFoundException("Service Not Found");
        }
        return dtoService.toTagDomainListDTOFromTagDomainServices(tagDomainServiceRepository.findByService(s));
    }


    // Experiment Methods----------------------------------------------------------------------------------------------
    @RequestMapping(value = {"experiments"}, method = RequestMethod.GET)
    public final List<ExperimentDTO> experiments(Principal principal) {
        kpiService.addEvent(principal, "api:experiments");
        accountingService.addMethod(principal, READ_ACTION, "experiments");
        return dtoService.toExperimentListDTO(experimentRepository.findAll());
    }

    @RequestMapping(value = {"experiments/{experimentUrn}"}, method = RequestMethod.GET)
    public final ExperimentDTO experiments(@PathVariable("experimentUrn") String experimentUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:experiments", "experimentUrn", experimentUrn);
        accountingService.addMethod(principal, READ_ACTION, "experiment", experimentUrn, null);
        Experiment a = experimentRepository.findByUrn(experimentUrn);
        if (a == null) {
            throw new NotFoundException("Experiment Not Found");
        }
        return dtoService.toDTO(a);
    }

    @RequestMapping(value = {"experiments/{experimentUrn}/tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomainDTO> experimentGetTagDomains(@PathVariable("experimentUrn") String experimentUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:experimentTagDomains", "experimentUrn", experimentUrn);
        accountingService.addMethod(principal, READ_ACTION, "experimentTagDomains", experimentUrn, null);
        Experiment a = experimentRepository.findByUrn(experimentUrn);
        if (a == null) {
            throw new NotFoundException("Experiment Not Found");
        }

        return dtoService.toTagDomainListDTO(experimentTagDomainRepository.findByExperiment(a).stream().map(ExperimentTagDomain::getTagDomain).collect(Collectors.toList()));
    }

    @RequestMapping(value = {"experiments/{experimentUrn}/annotations"}, method = RequestMethod.GET)
    public final Set<AnnotationDTO> experimentGetAnnotations(@PathVariable("experimentUrn") String experimentUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:experimentAnnotations", "experimentUrn", experimentUrn);
        accountingService.addMethod(principal, READ_ACTION, "experimentAnnotations", experimentUrn, null);

        Experiment a = experimentRepository.findByUrn(experimentUrn);
        if (a == null) {
            throw new NotFoundException("Experiment Not Found");
        }

        List<Tagging> totalTaggings = new ArrayList<>();
        List<ExperimentTagDomain> etds = experimentTagDomainRepository.findByExperiment(a);
        for (ExperimentTagDomain etd : etds) {
            List<Tag> tags = tagRepository.findByTagDomain(etd.getTagDomain());
            for (Tag tag : tags) {
                totalTaggings.addAll(taggingRepository.findByTag(tag));
            }

        }
        return dtoService.toAssetListDTO(totalTaggings);
    }


}
