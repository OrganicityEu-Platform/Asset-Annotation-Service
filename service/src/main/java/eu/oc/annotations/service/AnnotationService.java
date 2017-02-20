package eu.oc.annotations.service;

import eu.oc.annotations.config.Constants;
import eu.oc.annotations.domain.Annotation;
import eu.oc.annotations.domain.Application;
import eu.oc.annotations.domain.Tag;
import eu.oc.annotations.domain.TagDomain;
import eu.oc.annotations.domain.Tagging;
import eu.oc.annotations.handlers.RestException;
import eu.oc.annotations.repositories.TagDomainRepository;
import eu.oc.annotations.repositories.TagRepository;
import eu.oc.annotations.repositories.TaggingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by etheodor on 31/05/2016.
 */
@Service
public class AnnotationService {
    @Autowired
    TaggingRepository taggingRepository;


    @Autowired
    TagRepository tagRepository;
    @Autowired
    TagDomainRepository tagDomainRepository;

    public Annotation getAnnotation(Tagging t) {
        Annotation a = new Annotation();
        a.setAnnotationId(t.getId());
        a.setUser(t.getUser());
        a.setApplication(t.getApplication());
        a.setNumericValue(t.getNumericValue());
        a.setTextValue(t.getTextValue());
        if (t.getUrn() == null) throw new RestException("Null urn");
        a.setAssetUrn(t.getUrn());
        if (t.getTag() == null) throw new RestException("Null Tag Object");
        a.setTagUrn(t.getTag().getUrn());
        if (t.getTimestamp() == null) throw new RestException("Null Timestamp Object");
        a.setDatetime(Constants.epochToISODatetime(t.getTimestamp()));
        return a;
    }

    public Annotation createOrUpdateTagging(Annotation annotation) {
        Tag tag = tagRepository.findByUrn(annotation.getTagUrn());
        if (tag == null) throw new RestException("Provide a valid tag urn");
        TagDomain td = tagDomainRepository.findById(tag.getTagDomain().getId());

        Tagging tagg = new Tagging();
        Boolean exists = false;
        Tagging taggToDelete = null;
        for (Tagging t : taggingRepository.findByUrn(annotation.getAssetUrn())) {
            TagDomain tdT = tagDomainRepository.findById(t.getTag().getTagDomain().getId());
            if (t.getTag().getUrn().equals(tag.getUrn()) && t.getUrn().equals(annotation.getAssetUrn()) && t.getApplication().equals(annotation.getApplication()) && t.getUser().equals(annotation.getUser())) {
                tagg = t;
                exists = true;
                break;
            }
            if (tdT.getUrn().equals(td.getUrn())) { //There is a tagging
                taggToDelete = t;
            }
        }
        if (taggToDelete != null) {
            taggingRepository.delete(taggToDelete);
        }
        Long timestamp = System.currentTimeMillis();

        tagg.setTimestamp(timestamp);
        if (annotation.getNumericValue() != null) {
            if (annotation.getNumericValue().isInfinite() || annotation.getNumericValue().isNaN()) {
                throw new RestException("Provide a numeric value or else use the Text field");
            }
        }
        tagg.setNumericValue(annotation.getNumericValue());
        tagg.setTextValue(annotation.getTextValue());
        tagg.setUser(annotation.getUser()); //todo fix
        tagg.setApplication(annotation.getApplication()); //todo fix
        tagg.setUrn(annotation.getAssetUrn());
        tagg.setTag(tag);
        taggingRepository.save(tagg);
        return getAnnotation(tagg);
    }


    public Set<Annotation> getAllAnnotations() {
        Set<Annotation> annotations = new HashSet<>();
        for (Tagging tagging : taggingRepository.findAll()) {
            annotations.add(getAnnotation(tagging));
        }
        return annotations;
    }


    public Set<Annotation> getAnnotationsOfAsset(String assetUrn) {
        if (assetUrn.equals("*")) {
            return getAllAnnotations();
        }
        Set<Annotation> annotations = taggingRepository.findByUrn(assetUrn).stream().map(this::getAnnotation).collect(Collectors.toSet());
        return annotations;
    }

    public Annotation getAnnotationForAssetApplicationUserTag(String assetUrn, String application, String user, String tagUrn) {
        for (Tagging t : taggingRepository.findByUrn(assetUrn)) {
            if (t.getUser().equals(user) && t.getApplication().equals(application) && t.getTag().getUrn().equals(tagUrn)) {
                return getAnnotation(t);
            }
        }
        throw new RestException("No Annotation Found");
    }

    public Annotation getAnnotationForAssetApplicationUserTagDomain(String assetUrn, String application, String user, String tagDomain) {
        //todo add more
        for (Tagging t : taggingRepository.findByUrn(assetUrn)) {
            if (t.getUser().equals(user) && t.getApplication().equals(application)) {
                TagDomain td = tagDomainRepository.findById(t.getTag().getTagDomain().getId());
                if (td.getUrn().equals(tagDomain))
                    return getAnnotation(t);
            }
        }
        throw new RestException("No Annotation Found");
    }

    public void deleteAssetsAndAnnotations(String assetUrn) {
        try {
            if (assetUrn.equals("*"))
                taggingRepository.deleteAll();
            else {
                List<Tagging> taggings = taggingRepository.findByUrn(assetUrn);
                taggingRepository.delete(taggings);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException("Delete All Asset/Annotations Failed" + e.getMessage());
        }
    }


    public Collection<Application> findApplicationsUsingTagDomain(String tagDomainUrn) {
        return new ArrayList<>();
    }
}
