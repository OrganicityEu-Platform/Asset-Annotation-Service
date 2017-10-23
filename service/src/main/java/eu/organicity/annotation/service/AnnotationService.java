package eu.organicity.annotation.service;

import eu.organicity.annotation.common.dto.AnnotationStatisticsDTO;
import eu.organicity.annotation.domain.Annotation;
import eu.organicity.annotation.domain.Experiment;
import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.domain.Tagging;
import eu.organicity.annotation.handlers.RestException;
import eu.organicity.annotation.repositories.TagDomainRepository;
import eu.organicity.annotation.repositories.TagRepository;
import eu.organicity.annotation.repositories.TaggingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
    @Autowired
    OrionService orionService;
    
    public Annotation getAnnotation(Tagging t) {
        Annotation a = new Annotation();
        a.setAnnotationId(t.getId());
        a.setUser(t.getUser());
        a.setApplication(t.getApplication());
        a.setNumericValue(t.getNumericValue());
        a.setTextValue(t.getTextValue());
        if (t.getUrn() == null)
            throw new RestException("Null urn");
        a.setAssetUrn(t.getUrn());
        if (t.getTag() == null)
            throw new RestException("Null Tag Object");
        a.setTagUrn(t.getTag().getUrn());
        if (t.getLastModified() != null) {
            a.setDatetime(new Date(t.getLastModified()).toString());
        }
        return a;
    }
    
    public Annotation createOrUpdateTagging(Annotation annotation) {
        Tag tag = tagRepository.findByUrn(annotation.getTagUrn());
        if (tag == null)
            throw new RestException("Provide a valid tag urn");
        TagDomain td = tagDomainRepository.findById(tag.getTagDomain().getId());
        
        
        Tagging tagg = taggingRepository.findByUrnAndUserAndTag(annotation.getAssetUrn(), annotation.getUser(), tag);
        if (tagg == null) {
            tagg = new Tagging();
            tagg.setCreated(System.currentTimeMillis());
        }
        
        if (annotation.getNumericValue() != null) {
            if (annotation.getNumericValue().isInfinite() || annotation.getNumericValue().isNaN()) {
                throw new RestException("Provide a numeric value or else use the Text field");
            }
        }
        
        tagg.setLastModified(System.currentTimeMillis());
        tagg.setNumericValue(annotation.getNumericValue());
        tagg.setTextValue(annotation.getTextValue());
        tagg.setUser(annotation.getUser()); //todo fix
        tagg.setApplication(annotation.getApplication()); //todo fix
        tagg.setUrn(annotation.getAssetUrn());
        tagg.setTag(tag);
        taggingRepository.save(tagg);
        
        orionService.updateAnnotations(annotation.getAssetUrn());
        
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
            if ((user == null || t.getUser().equals(user)) && (application == null || t.getApplication().equals(application)) && (tagUrn == null || t.getTag().getUrn().equals(tagUrn))) {
                return getAnnotation(t);
            }
        }
        throw new RestException("No Annotation Found");
    }
    
    public Annotation getAnnotationForAssetApplicationUserTagDomain(String assetUrn, String application, String user, String tagDomain) {
        //todo add more
        for (Tagging t : taggingRepository.findByUrn(assetUrn)) {
            if ((user == null || t.getUser().equals(user)) && (application == null || t.getApplication().equals(application))) {
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
    
    
    public Collection<Experiment> findApplicationsUsingTagDomain(String tagDomainUrn) {
        return new ArrayList<>();
    }
    
    public AnnotationStatisticsDTO getAnnotationStatisticsOfAsset(String assetUrn) {
        final AnnotationStatisticsDTO dto = new AnnotationStatisticsDTO();
        
        dto.setAssetUrn(assetUrn);
        
        dto.setAnnotationsCount(taggingRepository.countByUrn(assetUrn));
        dto.setGlobalAnnotationsCount(taggingRepository.count());
        
        if (dto.getAnnotationsCount() > 0) {
            Tagging firstOfAsset = taggingRepository.findFirstOrderByCreatedAsc(assetUrn);
            dto.setFirstAnnotation(firstOfAsset.getCreated());
            Tagging lastOfAsset = taggingRepository.findFirstOrderByLastModifiedDesc(assetUrn);
            dto.setLastAnnotation(lastOfAsset.getLastModified());
        } else {
            dto.setLastAnnotation(0);
            dto.setFirstAnnotation(0);
        }
        
        dto.setGlobalFirstAnnotation(taggingRepository.findFirstOrderByCreatedAsc().getCreated());
        dto.setGlobalLastAnnotation(taggingRepository.findFirstOrderByLastModifiedDesc().getLastModified());
        
        Tag tag = tagRepository.findByUrn("urn:oc:tag:Reliability:Score");
        dto.setTotalRates(taggingRepository.countByUrnAndTag(assetUrn, tag));
        dto.setGlobalTotalRates(taggingRepository.countByTag(tag));
        List<Tagging> tags = taggingRepository.findByUrnAndTag(assetUrn, tag);
        double count = 0.0;
        long nums = 0L;
        for (Tagging tagging : tags) {
            count += tagging.getNumericValue();
            nums++;
        }
        if (nums == 0) {
            dto.setAssetRate(0);
        } else {
            dto.setAssetRate(count / nums);
        }
        
        return dto;
    }
}
