package eu.oc.annotations.controller;

import eu.oc.annotations.domain.Annotation;
import eu.oc.annotations.handlers.RestException;
import eu.oc.annotations.repositories.*;
import eu.oc.annotations.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class AnnotationController {

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

    // Annotation Tagging METHODS-----------------------------------------------
    //Create Tagging
    @RequestMapping(value = {"annotations/{assetUrn}"}, method = RequestMethod.POST)
    public final Annotation createAnnotation(@NotNull @PathVariable("assetUrn") String assetUrn, @NotNull @RequestBody Annotation annotation) {
        //Set of validations
        if (annotation.getAnnotationId() != null) throw new RestException("AnnotationID should be null");
        if (annotation.getDatetime() != null) throw new RestException("Datetime should be null");
        if (annotation.getUser() == null) throw new RestException("UserIdentification should not be null");
        if (annotation.getTagUrn() == null) throw new RestException("TagUrn should not be null");
        if (annotation.getApplication() == null) throw new RestException("Application should not be null");
        //todo validate assetUrn
        return annotationService.createOrUpdateTagging(annotation);
    }

/*
    @RequestMapping(value = {"annotations/all"}, method = RequestMethod.GET)
    public final List<Annotation> getAllAnnotations() {
        return annotationService.getAllAnnotations();
    }
*/

    @RequestMapping(value = {"annotations/{assetUrn}/all"}, method = RequestMethod.GET)
    public final List<Annotation> getAnnotations(@PathVariable("assetUrn") String assetUrn, final HttpServletResponse response) { //todo show all public Annotations of asset add paging and sorting
        response.setHeader("Cache-Control", "no-cache");
        return annotationService.getAnnotationsOfAsset(assetUrn);
    }


//    //Update Tagging
//    @RequestMapping(value = {"annotations/{assetUrn}"}, method = RequestMethod.PATCH)
//    public final Annotation updateAnnotation(@PathVariable("assetUrn") String assetUrn, @RequestParam(value="annotation", required=true) Annotation annotation) {
//        throw new RestException("Not Implemented yet!");
//    }

    //Delete Tagging
    @RequestMapping(value = {"annotations/{assetUrn}"}, method = RequestMethod.DELETE)
    public final Annotation deleteAnnotation(@PathVariable("assetUrn") String assetUrn, @RequestParam(value = "annotation", required = true) Annotation annotation) {
        throw new RestException("Not Implemented yet!");
    }


    @RequestMapping(value = {"annotations/"}, method = RequestMethod.GET) //todo fix
    public final Annotation getAnnotationForApplicationUserandTag(@RequestParam(value = "assetUrn", required = true) String assetUrn,
                                                                  @RequestParam(value = "applicationUrn", required = true) String applicationUrn,
                                                                  @RequestParam(value = "tagUrn", required = true) String tagUrn,
                                                                  @RequestParam(value = "user", required = true) String user
    ) {
        Annotation l = annotationService.getAnnotationForAssetApplicationUserTag(assetUrn, applicationUrn, user, tagUrn);
        return l;
    }


    @RequestMapping(value = {"annotations/{tagDomain}"}, method = RequestMethod.GET) //todo fix
    public final Annotation getAnnotationForApplication(@RequestParam(value = "assetUrn", required = true) String assetUrn,
                                                        @RequestParam(value = "applicationUrn", required = true) String applicationUrn,
                                                        @NotNull @PathVariable(value = "tagDomain") String tagDomain,
                                                        @RequestParam(value = "user", required = true) String user,
                                                        final HttpServletResponse response
    ) {
        response.setHeader("Cache-Control", "no-cache");
        Annotation l = annotationService.getAnnotationForAssetApplicationUserTagDomain(assetUrn, applicationUrn, user, tagDomain);
        return l;
    }


    @RequestMapping(value = {"admin/annotations/delete/{assetUrn}"}, method = RequestMethod.GET) //todo fix
    public final void delete(@PathVariable("assetUrn") String assetUrn
    ) {
        annotationService.deleteAssetsAndAnnotations(assetUrn);

    }

}
