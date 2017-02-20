package eu.organicity.annotation.controller;

import eu.organicity.annotation.config.OrganicityAccount;
import eu.organicity.annotation.domain.Annotation;
import eu.organicity.annotation.handlers.RestException;
import eu.organicity.annotation.repositories.TaggingRepository;
import eu.organicity.annotation.service.AnnotationService;
import eu.organicity.annotation.service.DTOService;
import eu.organicity.annotation.service.KPIService;
import eu.organicity.annotation.service.OrganicityUserDetailsService;
import eu.organicity.annotation.common.dto.AnnotationDTO;
import eu.organicity.annotation.common.dto.AssetAnnotationListDTO;
import eu.organicity.annotation.common.dto.AssetAnnotationListItemDTO;
import eu.organicity.annotation.common.dto.AssetListDTO;
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

@RestController
public class AnnotationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationController.class);

    @Autowired
    AnnotationService annotationService;
    @Autowired
    TaggingRepository taggingRepository;
    @Autowired
    KPIService kpiService;
    @Autowired
    DTOService dtoService;

    // Annotation Tagging METHODS-----------------------------------------------
    //Create Tagging
    @RequestMapping(value = {"annotations/{assetUrn}"}, method = RequestMethod.POST)
    public final Annotation createAnnotation(@NotNull @PathVariable("assetUrn") String assetUrn,
                                             @NotNull @RequestBody Annotation annotation, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/add", "assetUrn", assetUrn);

        //Set of validations
        if (annotation.getAnnotationId() != null) throw new RestException("AnnotationID should be null");
        if (annotation.getDatetime() != null) throw new RestException("Datetime should be null");
        if (annotation.getTagUrn() == null) throw new RestException("TagUrn should not be null");
        if (annotation.getApplication() == null) throw new RestException("Application should not be null");

        if (annotation.getUser() == null) {
            OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
            annotation.setUser(ou.getUser());
        } else {
            //annotation has a user already
        }

        return annotationService.createOrUpdateTagging(annotation);
    }

    @RequestMapping(value = {"annotations/{assetUrn}/all"}, method = RequestMethod.GET)
    public final Set<AnnotationDTO> getAnnotations(@PathVariable("assetUrn") String assetUrn, final HttpServletResponse response
            , Principal principal) {
        kpiService.addEvent(principal, "api:annotations", "assetUrn", assetUrn);

        //todo show all public Annotations of asset add paging and sorting
        response.setHeader("Cache-Control", "no-cache");
        return toDTO(annotationService.getAnnotationsOfAsset(assetUrn));
    }

    private Set<AnnotationDTO> toDTO(Set<Annotation> annotationsOfAsset) {
        final Set<AnnotationDTO> dtos = new HashSet<>();
        for (final Annotation annotation : annotationsOfAsset) {
            dtos.add(dtoService.toAnnotationDTO(annotation));
        }
        return dtos;
    }


    //Delete Tagging
    @RequestMapping(value = {"annotations/{assetUrn}"}, method = RequestMethod.DELETE)
    public final Annotation deleteAnnotation(@PathVariable("assetUrn") String assetUrn, @RequestParam(value = "annotation", required = true) Annotation annotation
            , Principal principal) {
        kpiService.addEvent(principal, "api:annotations/delete", "assetUrn", assetUrn);
        throw new RestException("Not Implemented yet!");
    }


    @RequestMapping(value = {"annotations/"}, method = RequestMethod.GET) //todo fix
    public final Annotation getAnnotationForApplicationUserandTag(@RequestParam(value = "assetUrn", required = true) String assetUrn,
                                                                  @RequestParam(value = "experimentUrn", required = true) String experimentUrn,
                                                                  @RequestParam(value = "tagUrn", required = true) String tagUrn,
                                                                  @RequestParam(value = "user", required = true) String user
            , Principal principal
    ) {
        kpiService.addEvent(principal, "api:annotations/query");
        return annotationService.getAnnotationForAssetApplicationUserTag(assetUrn, experimentUrn, user, tagUrn);
    }


    @RequestMapping(value = {"annotations/{tagDomain}"}, method = RequestMethod.GET) //todo fix
    public final Annotation getAnnotationForApplication(@RequestParam(value = "assetUrn", required = true) String assetUrn,
                                                        @RequestParam(value = "experimentUrn", required = true) String experimentUrn,
                                                        @NotNull @PathVariable(value = "tagDomain") String tagDomain,
                                                        @RequestParam(value = "user", required = true) String user,
                                                        final HttpServletResponse response
            , Principal principal
    ) {
        kpiService.addEvent(principal, "api:annotations", "tagDomain", tagDomain);
        response.setHeader("Cache-Control", "no-cache");
        return annotationService.getAnnotationForAssetApplicationUserTagDomain(assetUrn, experimentUrn, user, tagDomain);
    }


    @RequestMapping(value = {"admin/annotations/delete/{assetUrn}"}, method = RequestMethod.GET) //todo fix
    public final void delete(@PathVariable("assetUrn") String assetUrn, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/delete", "assetUrn", assetUrn);
        annotationService.deleteAssetsAndAnnotations(assetUrn);
    }

    @RequestMapping(value = {"annotations/all"}, method = RequestMethod.GET)
    public final Set<AnnotationDTO> getAnnotations(final HttpServletResponse response, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/all");

        //todo show all public Annotations of asset add paging and sorting
        response.setHeader("Cache-Control", "no-cache");

        return dtoService.toAssetListDTO(taggingRepository.findAll());
    }

    @RequestMapping(value = {"annotations/all"}, method = RequestMethod.POST)
    public final AssetAnnotationListDTO getAnnotations(final HttpServletResponse response, Principal principal,
                                                       @RequestBody final AssetListDTO assetListDTO) {
        kpiService.addEvent(principal, "api:annotations/all");

        final AssetAnnotationListDTO list = new AssetAnnotationListDTO();
        list.setAssetAnnotations(new HashSet<>());
        for (final String assetUrn : assetListDTO.getAssetUrns()) {
            final Set<Annotation> annotations = annotationService.getAnnotationsOfAsset(assetUrn);
            try {
                if (annotations != null) {
                    list.getAssetAnnotations().add(
                            new AssetAnnotationListItemDTO(assetUrn, annotations.size(), toDTO(annotations))
                    );
                } else {
                    list.getAssetAnnotations().add(
                            new AssetAnnotationListItemDTO(assetUrn, 0, new HashSet<>())
                    );
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
