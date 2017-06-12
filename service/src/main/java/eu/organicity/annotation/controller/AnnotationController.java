package eu.organicity.annotation.controller;

import eu.organicity.annotation.common.dto.AnnotationDTO;
import eu.organicity.annotation.common.dto.AnnotationStatisticsDTO;
import eu.organicity.annotation.common.dto.AssetAnnotationListDTO;
import eu.organicity.annotation.common.dto.AssetAnnotationListItemDTO;
import eu.organicity.annotation.common.dto.AssetListDTO;
import eu.organicity.annotation.config.OrganicityAccount;
import eu.organicity.annotation.domain.Annotation;
import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.handlers.RestException;
import eu.organicity.annotation.repositories.TagDomainRepository;
import eu.organicity.annotation.repositories.TagRepository;
import eu.organicity.annotation.repositories.TaggingRepository;
import eu.organicity.annotation.service.AccountingService;
import eu.organicity.annotation.service.AnnotationService;
import eu.organicity.annotation.service.DTOService;
import eu.organicity.annotation.service.KPIService;
import eu.organicity.annotation.service.OrganicityUserDetailsService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static eu.organicity.annotation.service.AccountingService.CREATE_ACTION;
import static eu.organicity.annotation.service.AccountingService.DELETE_ACTION;
import static eu.organicity.annotation.service.AccountingService.READ_ACTION;

@RestController
public class AnnotationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationController.class);
    
    @Autowired
    AnnotationService annotationService;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    TagDomainRepository tagDomainRepository;
    @Autowired
    TaggingRepository taggingRepository;
    @Autowired
    KPIService kpiService;
    @Autowired
    AccountingService accountingService;
    @Autowired
    DTOService dtoService;
    
    // Annotation Tagging METHODS-----------------------------------------------
    //Create Tagging
    @ApiOperation(value = "Create an Annotation", notes = "Provides means to create a new Annotation", nickname = "createAnnotation", response = Annotation.class)
    @RequestMapping(value = {"annotations/{assetUrn}"}, method = RequestMethod.POST)
    public final Annotation createAnnotation(@NotNull @PathVariable("assetUrn") String assetUrn, @NotNull @RequestBody Annotation annotation, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/add", "assetUrn", assetUrn);
        accountingService.addMethod(principal, CREATE_ACTION, "annotations/add", assetUrn, null);
        
        //Set of validations
        if (annotation.getAnnotationId() != null)
            throw new RestException("AnnotationID should be null");
        if (annotation.getDatetime() != null)
            throw new RestException("Datetime should be null");
        if (annotation.getTagUrn() == null)
            throw new RestException("TagUrn should not be null");
        if (annotation.getApplication() == null)
            throw new RestException("Experiment should not be null");
        
        if (annotation.getUser() == null) {
            OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
            annotation.setUser(ou.getUser());
        } else {
            //annotation has a user already
        }
        
        return annotationService.createOrUpdateTagging(annotation);
    }
    
    @ApiOperation(value = "List Asset Annotations", notes = "Provides means to list all Annotations of a single Asset", nickname = "getAnnotations", response = AnnotationDTO[].class)
    @RequestMapping(value = {"annotations/{assetUrn}/all"}, method = RequestMethod.GET)
    public final Set<AnnotationDTO> getAnnotations(@PathVariable("assetUrn") String assetUrn, final HttpServletResponse response, Principal principal) {
        kpiService.addEvent(principal, "api:annotations", "assetUrn", assetUrn);
        accountingService.addMethod(principal, READ_ACTION, "annotations", assetUrn, null);
        
        //todo show all public Annotations of asset add paging and sorting
        response.setHeader("Cache-Control", "no-cache");
        return toDTO(annotationService.getAnnotationsOfAsset(assetUrn));
    }
    
    
    @ApiOperation(value = "Show Asset Annotation Statistics", notes = "Provides means to retrieve statistics about the Annotations of a single Asset", nickname = "getAnnotations", response = AnnotationStatisticsDTO.class)
    @RequestMapping(value = {"annotations/{assetUrn}/statistics"}, method = RequestMethod.GET)
    public final AnnotationStatisticsDTO getAnnotationStatistics(@PathVariable("assetUrn") String assetUrn, final HttpServletResponse response, Principal principal) {
        
        //todo show all public Annotations of asset add paging and sorting
        response.setHeader("Cache-Control", "no-cache");
        return annotationService.getAnnotationStatisticsOfAsset(assetUrn);
    }
    
    
    private Set<AnnotationDTO> toDTO(Set<Annotation> annotationsOfAsset) {
        final Set<AnnotationDTO> dtos = new HashSet<>();
        for (final Annotation annotation : annotationsOfAsset) {
            String tag = annotation.getTagUrn();
            TagDomain tagDomain = tagRepository.findByUrn(tag).getTagDomain();
            dtos.add(dtoService.toAnnotationDTO(annotation, tagDomainRepository.findById(tagDomain.getId())));
        }
        return dtos;
    }
    
    
    //Delete Tagging
    @ApiOperation(value = "Delete Asset Annotation", notes = "Provides means to delete an Annotation of a single Asset", nickname = "deleteAnnotation", response = Annotation.class)
    @RequestMapping(value = {"annotations/{assetUrn}"}, method = RequestMethod.DELETE)
    public final Annotation deleteAnnotation(@PathVariable("assetUrn") String assetUrn, @RequestParam(value = "annotation", required = true) Annotation annotation, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/delete", "assetUrn", assetUrn);
        accountingService.addMethod(principal, DELETE_ACTION, "annotations/delete", assetUrn, null);
        throw new RestException("Not Implemented yet!");
    }
    
    
    @ApiOperation(value = "List Annotations of an Experiment, User and Tag", notes = "Provides means to list all Annotations of a single Experiment, User and Tag", nickname = "getAnnotationForExperimentUserandTag", response = Annotation.class)
    @RequestMapping(value = {"annotations/"}, method = RequestMethod.GET) //todo fix
    public final Annotation getAnnotationForExperimentUserandTag(@RequestParam(value = "assetUrn", required = true) String assetUrn, @RequestParam(value = "experimentUrn", required = true) String experimentUrn, @RequestParam(value = "tagUrn", required = true) String tagUrn, @RequestParam(value = "user", required = true) String user, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/query");
        accountingService.addMethod(principal, DELETE_ACTION, "annotations/query", assetUrn, null);
        return annotationService.getAnnotationForAssetApplicationUserTag(assetUrn, experimentUrn, user, tagUrn);
    }
    
    
    @ApiOperation(value = "List Annotations of an Experiment", notes = "Provides means to list all Annotations of a single Experiment", nickname = "getAnnotationForExperiment", response = Annotation.class)
    @RequestMapping(value = {"annotations/{tagDomain}"}, method = RequestMethod.GET) //todo fix
    public final Annotation getAnnotationForExperiment(@RequestParam(value = "assetUrn", required = true) String assetUrn, @RequestParam(value = "experimentUrn", required = true) String experimentUrn, @NotNull @PathVariable(value = "tagDomain") String tagDomain, @RequestParam(value = "user", required = true) String user, final HttpServletResponse response, Principal principal) {
        kpiService.addEvent(principal, "api:annotations", "tagDomain", tagDomain);
        response.setHeader("Cache-Control", "no-cache");
        return annotationService.getAnnotationForAssetApplicationUserTagDomain(assetUrn, experimentUrn, user, tagDomain);
    }
    
    
    @ApiOperation(value = "Delete all Annotations of an Asset", notes = "Provides means to delte all Annotations of a single Asset", nickname = "delete")
    @RequestMapping(value = {"admin/annotations/delete/{assetUrn}"}, method = RequestMethod.GET) //todo fix
    public final void delete(@PathVariable("assetUrn") String assetUrn, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/delete", "assetUrn", assetUrn);
        annotationService.deleteAssetsAndAnnotations(assetUrn);
    }
    
    @ApiOperation(value = "List Annotations of all Assets", notes = "Provides means to list all Annotations of all Assets", nickname = "getAnnotations", response = AnnotationDTO[].class)
    @RequestMapping(value = {"annotations/all"}, method = RequestMethod.GET)
    public final Set<AnnotationDTO> getAnnotations(final HttpServletResponse response, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/all");
        accountingService.addMethod(principal, READ_ACTION, "annotations/all");
        
        //todo show all public Annotations of asset add paging and sorting
        response.setHeader("Cache-Control", "no-cache");
        
        return dtoService.toAssetListDTO(taggingRepository.findAll());
    }
    
    @RequestMapping(value = {"annotations/all"}, method = RequestMethod.POST)
    public final AssetAnnotationListDTO getAnnotations(final HttpServletResponse response, Principal principal, @RequestBody final AssetListDTO assetListDTO) {
        kpiService.addEvent(principal, "api:annotations/all");
        
        
        final AssetAnnotationListDTO list = new AssetAnnotationListDTO();
        list.setAssetAnnotations(new HashSet<>());
        for (final String assetUrn : assetListDTO.getAssetUrns()) {
            final Set<Annotation> annotations = annotationService.getAnnotationsOfAsset(assetUrn);
            try {
                if (annotations != null) {
                    list.getAssetAnnotations().add(new AssetAnnotationListItemDTO(assetUrn, annotations.size(), toDTO(annotations)));
                } else {
                    list.getAssetAnnotations().add(new AssetAnnotationListItemDTO(assetUrn, 0, new HashSet<>()));
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                list.getAssetAnnotations().add(new AssetAnnotationListItemDTO(assetUrn, 0, new HashSet<>()));
                
            }
        }
        
        //todo show all public Annotations of assets add paging and sorting
        response.setHeader("Cache-Control", "no-cache");
        return list;
    }
    
}
