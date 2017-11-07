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
import eu.organicity.annotation.domain.TagDomainService;
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
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    @ApiOperation(value = "List all TagDomains", notes = "Provides means to list all available TagDomains", nickname = "domainFindAll", response = TagDomainDTO[].class)
    @RequestMapping(value = {"tagDomains"}, method = RequestMethod.GET)
    public final List<TagDomainDTO> domainFindAll(Principal principal) {
        kpiService.addEvent(principal, "api:tagDomains");
        accountingService.addMethod(principal, READ_ACTION, "tagDomains");
        
        //filter viewed domains
        final ArrayList<TagDomain> results = new ArrayList<>();
        for (final TagDomain tagDomain : tagDomainRepository.findAll()) {
            if (!tagDomain.getUrn().contains("urn:oc:tagDomain:experiments:")) {
                results.add(tagDomain);
            } else {
                if (principal != null && principal.getName().equals(tagDomain.getUser())) {
                    results.add(tagDomain);
                }
            }
        }
        
        return dtoService.toTagDomainListDTO(results);
    }
    
    
    //Get tagDomain
    @ApiOperation(value = "Get TagDomain", notes = "Provides means to list all properties of a single TagDomain", nickname = "domainFindByUrn", response = TagDomainDTO.class)
    @RequestMapping(value = {"tagDomains/{tagDomainUrn}"}, method = RequestMethod.GET)
    public final TagDomainDTO domainFindByUrn(@PathVariable("tagDomainUrn") String tagDomainUrn, Principal principal) {
        kpiService.addEvent(principal, "api:tagDomain", "tagDomainUrn", tagDomainUrn);
        accountingService.addMethod(principal, READ_ACTION, "domainFindByUrn", tagDomainUrn, null);
        return dtoService.toDTO(tagDomainRepository.findByUrn(tagDomainUrn));
    }
    
    //Get tagDomains suggested for an Asset
    @ApiOperation(value = "Get TagDomains for Asset", notes = "Provides means to list proposed TagDomains for a specific Asset", nickname = "domainsFindByAssetUrn", response = TagDomainDTO.class)
    @RequestMapping(value = {"tagDomains/asset/{asset}"}, method = RequestMethod.GET)
    public final Set<TagDomainDTO> domainsFindByAssetUrn(@PathVariable("asset") String assetUrn, Principal principal) {
        //kpiService.addEvent(principal, "api:tagDomain", "tagDomainUrn", tagDomainUrn);
        //accountingService.addMethod(principal, READ_ACTION, "domainFindByUrn", tagDomainUrn, null);
        Set<TagDomainDTO> dto = new HashSet<>();
        if (assetUrn.startsWith("urn:oc:entity:experimenters:")) {
            try {
                final String experimentId = assetUrn.split(":")[5];
                dto.add(getTagDomain("urn:oc:tagDomain:experiments:" + experimentId));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            
            if (dto.isEmpty()) {
                try {
                    final String experimentId = assetUrn.split(":")[5];
                    dto.addAll(getExperimentTagDomains(experimentId));
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            
            if (dto.isEmpty()) {
                try {
                    final String experimenterId = assetUrn.split(":")[4];
                    dto.addAll(getUserTagDomains(experimenterId));
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        } else {
            //city asset
            if (assetUrn.contains("traffic")) {
                dto.add(dtoService.toDTO(tagDomainRepository.findByUrn("urn:oc:tagDomain:TrafficLevel")));
            } else if (assetUrn.contains("bikestation")) {
                dto.add(dtoService.toDTO(tagDomainRepository.findByUrn("urn:oc:tagDomain:EmptyStation")));
            } else if (assetUrn.contains("luminosity")) {
                dto.add(dtoService.toDTO(tagDomainRepository.findByUrn("urn:oc:tagDomain:LightLevel")));
            } else if (assetUrn.contains("noise")) {
                dto.add(dtoService.toDTO(tagDomainRepository.findByUrn("urn:oc:tagDomain:AnomalyDetection")));
            }
        }
        
        //last chance
        if (dto.isEmpty()) {
            dto.add(getTagDomain("urn:oc:tagDomain:AnomalyDetection"));
        }
        return dto;
    }
    
    private Set<TagDomainDTO> getUserTagDomains(final String experimenterId) {
        final Set<TagDomainDTO> dto = new HashSet<>();
        final Set<TagDomain> tagDomains = tagDomainRepository.findByUser(experimenterId);
        for (final TagDomain tagDomain : tagDomains) {
            dto.add(dtoService.toDTO(tagDomain));
        }
        return dto;
    }
    
    private Set<TagDomainDTO> getExperimentTagDomains(final String experimentId) {
        final Set<TagDomainDTO> dto = new HashSet<>();
        final Experiment experiment = experimentRepository.findByUrn("urn:oc:entity:experiments:" + experimentId);
        final List<ExperimentTagDomain> expT = experimentTagDomainRepository.findByExperiment(experiment);
        for (final ExperimentTagDomain experimentTagDomain : expT) {
            dto.add(dtoService.toDTO(tagDomainRepository.findById(experimentTagDomain.getId())));
        }
        return dto;
    }
    
    private TagDomainDTO getTagDomain(final String tagDomainUrn) {
        return dtoService.toDTO(tagDomainRepository.findByUrn(tagDomainUrn));
    }
    
    // TAG METHODS-----------------------------------------------
    @ApiOperation(value = "Get Tags of a TagDomain", notes = "Provides means to list all Tags of a single TagDomain", nickname = "domainGetTags", response = TagDTO[].class)
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
    
    @ApiOperation(value = "Get a Tag", notes = "Provides means to list all properties of a single Tag", nickname = "tag", response = TagDTO.class)
    @RequestMapping(value = {"tags/{tagUrn}"}, method = RequestMethod.GET)
    public final TagDTO tag(@PathVariable("tagUrn") String tagUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:tag", "tagUrn", tagUrn);
        accountingService.addMethod(principal, READ_ACTION, "tag", tagUrn, null);
        Tag t = tagRepository.findByUrn(tagUrn);
        if (t == null) {
            throw new NotFoundException("Tag Not Found");
        }
        return dtoService.toDTO(t);
    }
    
    // Services METHODS-----------------------------------------------
    @ApiOperation(value = "Get Services", notes = "Provides means to list all Services", nickname = "services", response = ServiceDTO[].class)
    @RequestMapping(value = {"services"}, method = RequestMethod.GET)
    public final List<ServiceDTO> services(Principal principal) {
        kpiService.addEvent(principal, "api:services");
        accountingService.addMethod(principal, READ_ACTION, "services");
        return dtoService.toOriginalServiceListDTO(serviceRepository.findAll());
    }
    
    @ApiOperation(value = "Get a Service", notes = "Provides means to list all properties of a Service", nickname = "service", response = ServiceDTO.class)
    @RequestMapping(value = {"services/{serviceUrn}"}, method = RequestMethod.GET)
    public final ServiceDTO service(@PathVariable("serviceUrn") String serviceUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:service", "serviceUrn", serviceUrn);
        accountingService.addMethod(principal, READ_ACTION, "service", serviceUrn, null);
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new NotFoundException("Service Not Found");
        }
        return dtoService.toDTO(s);
    }
    
    @ApiOperation(value = "Get TagDomains of a Service", notes = "Provides means to list all TagDomains of a single Service", nickname = "serviceGetTagDomains", response = TagDomainDTO[].class)
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
    @ApiOperation(value = "Get all Experiments", notes = "Provides means to list all Experiments", nickname = "experiments", response = ExperimentDTO[].class)
    @RequestMapping(value = {"experiments"}, method = RequestMethod.GET)
    public final List<ExperimentDTO> experiments(Principal principal) {
        kpiService.addEvent(principal, "api:experiments");
        accountingService.addMethod(principal, READ_ACTION, "experiments");
        return dtoService.toExperimentListDTO(experimentRepository.findAll());
    }
    
    @ApiOperation(value = "Get an Experiment", notes = "Provides means to list all properties of an Experiment", nickname = "experiment", response = ExperimentDTO.class)
    @RequestMapping(value = {"experiments/{experimentUrn}"}, method = RequestMethod.GET)
    public final ExperimentDTO experiment(@PathVariable("experimentUrn") String experimentUrn, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:experiments", "experimentUrn", experimentUrn);
        accountingService.addMethod(principal, READ_ACTION, "experiment", experimentUrn, null);
        Experiment a = experimentRepository.findByUrn(experimentUrn);
        if (a == null) {
            throw new NotFoundException("Experiment Not Found");
        }
        return dtoService.toDTO(a);
    }
    
    @ApiOperation(value = "Get the TagDomains of an Experiment", notes = "Provides means to list all TagDomains of an Experiment", nickname = "experimentGetTagDomains", response = TagDomainDTO[].class)
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
    
    @ApiOperation(value = "Get the Annotations of an Experiment", notes = "Provides means to list all Annotations of an Experiment", nickname = "experimentGetAnnotations", response = AnnotationDTO[].class)
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
    
    
    @ApiOperation(value = "Search in the TagDomains, Tags and Services for a set of keywords", notes = "Provides means to find all TagDomains that can possibly connected with the provided keyworks", nickname = "search", response = TagDomainDTO[].class)
    @RequestMapping(value = {"search"}, method = RequestMethod.GET)
    public final List<TagDomainDTO> search(@RequestParam("query") List<String> query, Principal principal) throws NotFoundException {
        kpiService.addEvent(principal, "api:search");
        accountingService.addMethod(principal, READ_ACTION, "search");
        
        final Set<TagDomain> domains = new HashSet<>();
        
        for (final String queryItem : query) {
            final Set<Service> services = serviceRepository.findByDescriptionContaining(queryItem);
            for (final Service service : services) {
                final List<TagDomainService> tagDomainServices = tagDomainServiceRepository.findByService(service);
                for (final TagDomainService tagDomainService : tagDomainServices) {
                    if (tagDomainService.getTagDomain() != null && tagDomainService.getTagDomain().getId() != null) {
                        domains.add(tagDomainRepository.findById(tagDomainService.getTagDomain().getId()));
                    }
                }
            }
        }
        
        for (final String queryItem : query) {
            final Set<TagDomain> currentDomains = tagDomainRepository.findByDescriptionContaining(queryItem);
            domains.addAll(currentDomains);
        }
        
        for (final String queryItem : query) {
            final Set<Tag> tags = tagRepository.findByNameContaining(queryItem);
            for (final Tag tag : tags) {
                if (tag.getTagDomain() != null && tag.getTagDomain().getId() != null)
                    domains.add(tagDomainRepository.findById(tag.getTagDomain().getId()));
            }
        }
        
        return dtoService.toTagDomainListDTO(domains);
    }
    
    
}
