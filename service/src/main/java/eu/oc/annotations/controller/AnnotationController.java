package eu.oc.annotations.controller;

import eu.oc.annotations.config.OrganicityAccount;
import eu.oc.annotations.domain.Annotation;
import eu.oc.annotations.domain.Asset;
import eu.oc.annotations.handlers.RestException;
import eu.oc.annotations.repositories.AssetRepository;
import eu.oc.annotations.service.AnnotationService;
import eu.oc.annotations.service.KPIService;
import eu.oc.annotations.service.OrganicityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
public class AnnotationController {

    @Autowired
    AnnotationService annotationService;

    @Autowired
    AssetRepository assetRepository;
    @Autowired
    KPIService kpiService;

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
    public final List<Annotation> getAnnotations(@PathVariable("assetUrn") String assetUrn, final HttpServletResponse response
            , Principal principal) {
        kpiService.addEvent(principal, "api:annotations", "assetUrn", assetUrn);

        //todo show all public Annotations of asset add paging and sorting
        response.setHeader("Cache-Control", "no-cache");
        return annotationService.getAnnotationsOfAsset(assetUrn);
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
                                                                  @RequestParam(value = "applicationUrn", required = true) String applicationUrn,
                                                                  @RequestParam(value = "tagUrn", required = true) String tagUrn,
                                                                  @RequestParam(value = "user", required = true) String user
            , Principal principal
    ) {
        kpiService.addEvent(principal, "api:annotations/query");
        return annotationService.getAnnotationForAssetApplicationUserTag(assetUrn, applicationUrn, user, tagUrn);
    }


    @RequestMapping(value = {"annotations/{tagDomain}"}, method = RequestMethod.GET) //todo fix
    public final Annotation getAnnotationForApplication(@RequestParam(value = "assetUrn", required = true) String assetUrn,
                                                        @RequestParam(value = "applicationUrn", required = true) String applicationUrn,
                                                        @NotNull @PathVariable(value = "tagDomain") String tagDomain,
                                                        @RequestParam(value = "user", required = true) String user,
                                                        final HttpServletResponse response
            , Principal principal
    ) {
        kpiService.addEvent(principal, "api:annotations", "tagDomain", tagDomain);
        response.setHeader("Cache-Control", "no-cache");
        return annotationService.getAnnotationForAssetApplicationUserTagDomain(assetUrn, applicationUrn, user, tagDomain);
    }


    @RequestMapping(value = {"admin/annotations/delete/{assetUrn}"}, method = RequestMethod.GET) //todo fix
    public final void delete(@PathVariable("assetUrn") String assetUrn, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/delete", "assetUrn", assetUrn);
        annotationService.deleteAssetsAndAnnotations(assetUrn);
    }

    @RequestMapping(value = {"annotations/all"}, method = RequestMethod.GET)
    public final List<Asset> getAnnotations(final HttpServletResponse response, Principal principal) {
        kpiService.addEvent(principal, "api:annotations/all");
        //todo show all public Annotations of asset add paging and sorting
        response.setHeader("Cache-Control", "no-cache");
        return assetRepository.findAll();
    }


}
