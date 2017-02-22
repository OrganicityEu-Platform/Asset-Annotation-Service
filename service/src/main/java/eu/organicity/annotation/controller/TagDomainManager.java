package eu.organicity.annotation.controller;

import eu.organicity.annotation.common.dto.ExperimentDTO;
import eu.organicity.annotation.common.dto.ServiceDTO;
import eu.organicity.annotation.common.dto.TagDTO;
import eu.organicity.annotation.common.dto.TagDomainDTO;
import eu.organicity.annotation.config.OrganicityAccount;
import eu.organicity.annotation.domain.Experiment;
import eu.organicity.annotation.domain.ExperimentTagDomain;
import eu.organicity.annotation.domain.Service;
import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.domain.TagDomainService;
import eu.organicity.annotation.domain.Tagging;
import eu.organicity.annotation.handlers.RestException;
import eu.organicity.annotation.repositories.ExperimentRepository;
import eu.organicity.annotation.repositories.ExperimentTagDomainRepository;
import eu.organicity.annotation.repositories.ServiceRepository;
import eu.organicity.annotation.repositories.TagDomainRepository;
import eu.organicity.annotation.repositories.TagDomainServiceRepository;
import eu.organicity.annotation.repositories.TagRepository;
import eu.organicity.annotation.repositories.TaggingRepository;
import eu.organicity.annotation.service.AnnotationService;
import eu.organicity.annotation.service.DTOService;
import eu.organicity.annotation.service.KPIService;
import eu.organicity.annotation.service.OrganicityUserDetailsService;
import eu.organicity.annotation.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class TagDomainManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagDomainManager.class);
    
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
    AnnotationService annotationService;
    
    @Autowired
    TagDomainServiceRepository tagDomainServiceRepository;
    
    @Autowired
    ExperimentTagDomainRepository experimentTagDomainRepository;
    
    @Autowired
    KPIService kpiService;
    
    @Autowired
    DTOService dtoService;
    
    @Autowired
    SecurityService securityService;
    
    // TAG DOMAIN METHODS--------------------------------------------------------------------------------
    
    //Create tagDomain
    @RequestMapping(value = {"admin/tagDomains"}, method = RequestMethod.POST)
    public final TagDomainDTO domainCreate(@RequestBody TagDomainDTO dto, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains", "tagDomainUrn", dto.getUrn());
        
        LOGGER.info("POST domainCreate");
        
        TagDomain d = tagDomainRepository.findByUrn(dto.getUrn());
        if (d != null) { //tagDomain Create
            LOGGER.error("TagDomain Exception: duplicate urn");
            throw new RestException("TagDomain Exception: duplicate urn");
        }
        
        LOGGER.info("checking permissions");
        
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canCreate(ou)) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        LOGGER.info("has access");
        
        TagDomain domain = new TagDomain();
        domain.setUrn(dto.getUrn());
        domain.setDescription(dto.getDescription());
        if (dto.getUser() != null) {
            domain.setUser(dto.getUser());
        } else {
            domain.setUser(ou.getUser());
        }
        domain.setTags(new ArrayList<>());
        try {
            LOGGER.info("saving domain " + domain);
            domain = tagDomainRepository.save(domain);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RestException(e.getMessage());
        }
        if (dto.getTags() != null) {
            LOGGER.info("adding tags");
            for (TagDTO tagDTO : dto.getTags()) {
                LOGGER.info("adding tag " + tagDTO);
                addTag2Domain(domain, tagDTO);
            }
        }
        
        domain = tagDomainRepository.findByUrn(dto.getUrn());
        LOGGER.info("returning : " + domain);
        return dtoService.toDTO(domain, tagRepository.findByTagDomain(domain));
    }
    
    //Update tagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}"}, method = RequestMethod.POST)
    public final TagDomainDTO domainUpdate(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody TagDomainDTO domain, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/update", "tagDomainUrn", domain.getUrn());
        
        LOGGER.info("POST domainUpdate");
        
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            LOGGER.error("TagDomain Not Found");
            throw new RestException("TagDomain Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canEdit(d, ou)) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        
        if (ou.isTheOnlyExperimnterUsingTagDomain(annotationService.findApplicationsUsingTagDomain(tagDomainUrn))) {
            LOGGER.error("TagDomain is used also from other experiments. Not possible to delete/update");
            throw new RestException("TagDomain is used also from other experiments. Not possible to delete/update");
        }
        d.setDescription(domain.getDescription());
        d.setUser(ou.getUser());
        try {
            d = tagDomainRepository.save(d);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RestException(e.getMessage());
        }
        return dtoService.toDTO(d);
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
        
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canEdit(d, ou)) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.isTheOnlyExperimnterUsingTagDomain(annotationService.findApplicationsUsingTagDomain(tagDomainUrn))) {
            LOGGER.error("TagDomain is used also from other experiments. Not possible to delete/update");
            throw new RestException("TagDomain is used also from other experiments. Not possible to delete/update");
        }
        
        final List<Tag> tags = tagRepository.findByTagDomain(d);
        
        if (tags != null && !tags.isEmpty()) {
            LOGGER.error("TagDomain is not empty");
            //throw new RestException("TagDomain is not empty");
            for (final Tag tag : tags) {
                domainRemoveTag(d, tag.getUrn());
            }
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
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/tag"}, method = RequestMethod.POST)
    public final TagDTO domainCreateTag(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody TagDTO tag, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/tags/add", "tagDomainUrn", tagDomainUrn, "tagUrn", tag.getUrn());
        
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        
        LOGGER.info("POST domainCreateTag");
        Tag addedTag = addTag2Domain(d, tag);
        LOGGER.info("addedTag:" + addedTag);
        return dtoService.toDTO(addedTag);
    }
    
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.POST)
    public final Set<TagDTO> domainCreateTags(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody List<TagDTO> tags, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/tags/add", "tagDomainUrn", tagDomainUrn, "tags", tags);
        
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        
        LOGGER.info("POST domainCreateTags");
        Set<Tag> addedTags = new HashSet<>();
        for (final TagDTO tag : tags) {
            addedTags.add(addTag2Domain(d, tag));
        }
        return dtoService.toTagSetDTO(addedTags);
    }
    
    //delete Tag from TagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.DELETE)
    public final void domainRemoveTag(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody String tagUrnList, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/tags/delete", "tagDomainUrn", tagDomainUrn, "tagUrn", tagUrnList);
        
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canEdit(d, ou)) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        
        try {
            if (ou.isTheOnlyExperimnterUsingTagDomain(annotationService.findApplicationsUsingTagDomain(tagDomainUrn))) {
                LOGGER.info("");
                throw new RestException("TagDomain is used also from other experiments. Not possible to delete/update");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        domainRemoveTag(d, tagUrnList);
    }
    
    //delete Tag from TagDomain
    public final void domainRemoveTag(TagDomain d, String tagUrnList) {
        LOGGER.info("DELETE domainRemoveTag " + tagUrnList);
        
        
        for (String tagUrn : tagUrnList.split(",")) {
            tagUrn = tagUrn.replace("\"", "");
            Tag t = tagRepository.findByUrn(tagUrn);
            if (t == null) {
                continue;
            }
            
            try {
                
                LOGGER.info("Deleting taggings for " + t.getUrn());
                List<Tagging> taggings = taggingRepository.findByTag(t);
                taggingRepository.delete(taggings);
                
                LOGGER.info("Deleting tag: " + t.getId());
                tagRepository.delete(t.getId());
                
                
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestException(e.getMessage());
            }
        }
        
    }
    
    //get Services using this TagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.GET)
    public final List<ServiceDTO> tagDomainGetServices(@PathVariable("tagDomainUrn") String tagDomainUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/services", "tagDomainUrn", tagDomainUrn);
        
        LOGGER.info("GET tagDomainGetServices");
        
        TagDomain a = tagDomainRepository.findByUrn(tagDomainUrn);
        if (a == null) {
            throw new RestException("TagDomain Not Found");
        }
        return dtoService.toServiceListDTO(tagDomainServiceRepository.findByTagDomain(a));
    }
    
    //Associate TagDomain with Service
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.POST)
    public final TagDomainDTO serviceAddTagDomains(@RequestParam(value = "serviceUrn") String serviceUrn, @PathVariable("tagDomainUrn") String tagDomainUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/services/add", "tagDomainUrn", tagDomainUrn, "serviceUrn", serviceUrn);
        
        LOGGER.info("POST serviceAddTagDomains");
        
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canEdit(d, ou) || !securityService.canEdit(s, ou)) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        try {
            TagDomainService ds = new TagDomainService();
            ds.setTagDomain(d);
            ds.setService(s);
            return dtoService.toDTO(tagDomainServiceRepository.save(ds).getTagDomain());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }
    
    //Disassociate TagDomain with Service
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.DELETE)
    public final void serviceRemoveTagDomains(@RequestParam(value = "serviceUrn") String serviceUrn, @PathVariable("tagDomainUrn") String tagDomainUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/tagDomains/services/remove", "tagDomainUrn", tagDomainUrn, "serviceUrn", serviceUrn);
        
        LOGGER.info("DELETE serviceRemoveTagDomains");
        
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canEdit(d, ou) || !securityService.canEdit(s, ou)) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        TagDomainService ds = tagDomainServiceRepository.findByServiceAndTagDomain(s, d);
        tagDomainServiceRepository.delete(ds);
    }
    
    
    // Services METHODS-----------------------------------------------
    
    //Create Service
    @RequestMapping(value = {"admin/services"}, method = RequestMethod.POST)
    public final ServiceDTO servicesCreate(@RequestBody ServiceDTO dto, Principal principal) {
        kpiService.addEvent(principal, "api:admin/services/create", "serviceUrn", dto.getUrn());
        
        LOGGER.info("POST servicesCreate");
        
        Service s = serviceRepository.findByUrn(dto.getUrn());
        if (s != null) { //tagDomain Create
            throw new RestException("Service Exception: duplicate urn");
        }
        LOGGER.info("check permissions");
        
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canCreateService(ou)) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        LOGGER.info("can create");
        s = new Service();
        s.setId(null);
        s.setUrn(dto.getUrn());
        s.setDescription(dto.getDescription());
        if (dto.getUrn() != null) {
            s.setUser(dto.getUser());
        } else {
            s.setUser(ou.getId());
        }
        s = serviceRepository.save(s);
        return dtoService.toDTO(s);
    }
    
    //Delete Service
    @RequestMapping(value = {"admin/services/{serviceUrn}"}, method = RequestMethod.DELETE)
    public final void serviceDelete(@PathVariable("serviceUrn") String serviceUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/services/delete", "serviceUrn", serviceUrn);
        
        LOGGER.info("DELETE serviceDelete");
        
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canEdit(s, ou)) {
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
    
    
    // Experiment METHODS-----------------------------------------------
    
    //Create Experiment
    @RequestMapping(value = {"admin/experiments"}, method = RequestMethod.POST)
    public final ExperimentDTO experimentsCreate(@RequestBody ExperimentDTO experimentDTO, Principal principal) {
        kpiService.addEvent(principal, "api:admin/experiments/create", "experimentUrn", experimentDTO.getUrn());
        
        System.out.println("POST experimentsCreate");
        
        if (experimentDTO.getId() != null) {
            throw new RestException("Experiment Exception: Experiment.id has to be null");
        }
        Experiment a = experimentRepository.findByUrn(experimentDTO.getUrn());
        if (a != null) { //tagDomain Create
            throw new RestException("Experiment Exception: duplicate urn");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (ou == null || (!ou.isAdministrator() && !ou.isExperimenter())) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        
        String tagDomainUrn = experimentDTO.getUrn().replaceAll(":entity:experiments:", ":tagDomain:experiments:");
        
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d != null) { //tagDomain Create
            LOGGER.error("TagDomain Exception: duplicate urn");
            throw new RestException("TagDomain Exception: duplicate urn");
        }
        
        TagDomain domain = new TagDomain();
        domain.setUrn(tagDomainUrn);
        domain.setDescription("Experiment Tag Domain");
        domain.setTags(new ArrayList<>());
        domain.setUser(ou.getUser());
        try {
            LOGGER.info("saving domain " + domain);
            domain = tagDomainRepository.save(domain);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RestException(e.getMessage());
        }
        
        Experiment experiment = new Experiment();
        experiment.setId(null);
        experiment.setUrn(experimentDTO.getUrn());
        experiment.setDescription(experimentDTO.getDescription());
        experiment.setUser(ou.getUser());
        try {
            experiment = experimentRepository.save(experiment);
            
            ExperimentTagDomain etd = new ExperimentTagDomain();
            etd.setExperiment(experiment);
            etd.setTagDomain(domain);
            experimentTagDomainRepository.save(etd);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return dtoService.toDTO(experiment);
    }
    
    
    //Delete Experiment
    @RequestMapping(value = {"admin/experiments/{experimentUrn}"}, method = RequestMethod.DELETE)
    public final void experimentDelete(@PathVariable("experimentUrn") String experimentUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/experiments/delete", "experimentUrn", experimentUrn);
        
        LOGGER.info("DELETE experimentDelete");
        
        Experiment a = experimentRepository.findByUrn(experimentUrn);
        if (a == null) {
            throw new RestException("Experiment Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (ou == null || (!ou.isAdministrator() && !ou.isExperimenter())) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.ownsExperiment(experimentUrn)) {
            throw new RestException("Experimenter is not owning experiment");
        }
        //todo extra checks (e.g. existing taggings etc)
        try {
            experimentRepository.delete(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }
    
    //Show all available tag domains for application/experiment
    @RequestMapping(value = {"admin/experiments/{experimentUrn}/tagDomains"}, method = RequestMethod.GET) //todo
    public final List<TagDomainDTO> applicationGetTagDomains(@PathVariable("experimentUrn") String experimentUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/experiments/tagDomains", "experimentUrn", experimentUrn);
        
        LOGGER.info("GET experimentGetTagDomains");
        
        Experiment a = experimentRepository.findByUrn(experimentUrn);
        if (a == null) {
            throw new RestException("Experiment Not Found");
        }
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (ou == null || (!ou.isAdministrator() && !ou.isExperimenter())) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        if (ou.ownsExperiment(experimentUrn)) {
            throw new RestException("Experimenter is not owning experiment");
        }
        
        return dtoService.toTagDomainListDTO(experimentTagDomainRepository.findByExperiment(a).stream().map(ExperimentTagDomain::getTagDomain).collect(Collectors.toList()));
    }
    
    
    @RequestMapping(value = {"admin/experiments/{experimentUrn}/tagDomains"}, method = RequestMethod.POST)
    public final ExperimentDTO experimentAddTagDomains(@RequestParam(value = "tagDomainUrn", required = true) List<String> tagDomainUrns, @PathVariable("experimentUrn") String experimentUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/experiments/tagDomains/add", "experimentUrn", experimentUrn, "tagDomainUrns", tagDomainUrns);
        
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (ou == null || (!ou.isAdministrator() && !ou.isExperimenter())) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        
        LOGGER.info("POST experimentAddTagDomains");
        
        for (String tagDomainUrn : tagDomainUrns) {
            
            Experiment a = experimentRepository.findByUrn(experimentUrn);
            if (a == null) {
                throw new RestException("Experiment Not Found");
            }
            TagDomain td = tagDomainRepository.findByUrn(tagDomainUrn);
            if (td == null) {
                throw new RestException("TagDomain Not Found");
            }
            
            if (ou.ownsExperiment(experimentUrn)) {
                throw new RestException("Experimenter is not owning experiment");
            }
            
            try {
                ExperimentTagDomain etd = experimentTagDomainRepository.findByExperimentAndTagDomain(a, td);
                if (etd == null) {
                    etd = new ExperimentTagDomain();
                    etd.setExperiment(a);
                    etd.setTagDomain(td);
                    experimentTagDomainRepository.save(etd);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestException(e.getMessage());
            }
        }
        
        return dtoService.toDTO(experimentRepository.findByUrn(experimentUrn));
        
    }
    
    @RequestMapping(value = {"admin/experiments/{experimentUrn}/tagDomains"}, method = RequestMethod.DELETE)
    public final void experimentRemoveTagDomains(@RequestParam(value = "tagDomainUrn", required = true) List<String> tagDomainUrns, @PathVariable("experimentUrn") String experimentUrn, Principal principal) {
        kpiService.addEvent(principal, "api:admin/experiments/tagDomains/remove", "experimentUrn", experimentUrn, "tagDomainUrns", tagDomainUrns);
        
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (ou == null || (!ou.isAdministrator() && !ou.isExperimenter())) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        
        LOGGER.info("DELETE experimentRemoveTagDomains");
        
        for (String tagDomainUrn : tagDomainUrns) {
            
            Experiment a = experimentRepository.findByUrn(experimentUrn);
            if (a == null) {
                throw new RestException("Experiment Not Found");
            }
            TagDomain td = tagDomainRepository.findByUrn(tagDomainUrn);
            if (td == null) {
                throw new RestException("TagDomain Not Found");
            }
            
            if (ou.ownsExperiment(experimentUrn)) {
                throw new RestException("Experimenter is not owning experiment");
            }
            try {
                ExperimentTagDomain etd = experimentTagDomainRepository.findByExperimentAndTagDomain(a, td);
                experimentTagDomainRepository.delete(etd);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestException(e.getMessage());
            }
        }
    }
    
    //Add tag to domain
    
    private Tag addTag2Domain(TagDomain d, TagDTO dto) {
        if (d == null) {
            LOGGER.error("TagDomain Not Found");
            throw new RestException("TagDomain Not Found");
        }
        
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (!securityService.canEdit(d, ou)) {
            LOGGER.error("Not Authorized Access");
            throw new RestException("Not Authorized Access");
        }
        
        if (ou.isTheOnlyExperimnterUsingTagDomain(annotationService.findApplicationsUsingTagDomain(d.getUrn()))) {
            LOGGER.error("TagDomain is used also from other experiments. Not possible to delete/update");
            throw new RestException("TagDomain is used also from other experiments. Not possible to delete/update");
        }
        
        Tag tag = tagRepository.findByUrn(dto.getUrn());
        try {
            if (tag == null) {
                tag = new Tag();
            }
            tag.setId(null);
            tag.setUrn(dto.getUrn());
            tag.setName(dto.getName());
            
            if (dto.getUser() != null) {
                tag.setUser(dto.getUser());
            } else {
                tag.setUser(ou.getUser());
            }
            
            tag.setTagDomain(d);
            tag = tagRepository.save(tag);
            LOGGER.info("tag:" + tag);
            return tag;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RestException(e.getMessage());
        }
    }
}
