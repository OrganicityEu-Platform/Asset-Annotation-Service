package eu.oc.annotations.service;

import eu.oc.annotations.config.Constants;
import eu.oc.annotations.domain.*;
import eu.oc.annotations.handlers.RestException;
import eu.oc.annotations.repositories.AssetRepository;
import eu.oc.annotations.repositories.TagDomainRepository;
import eu.oc.annotations.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by etheodor on 31/05/2016.
 */
@Service
public class AnnotationService {
    @Autowired
    AssetRepository assetRepository;


    @Autowired
    TagRepository tagRepository;
    @Autowired
    TagDomainRepository tagDomainRepository;

    public Annotation getAnnotation(Tagging t) {
        Annotation a = new Annotation();
        a.setAnnotationId(t.getTaggingId());
        a.setUser(t.getUser());
        a.setApplication(t.getApplication());
        a.setNumericValue(t.getNumericValue());
        a.setTextValue(t.getTextValue());
        if (t.getAsset() == null) throw new RestException("Null Asset Object");
        a.setAssetUrn(t.getAsset().getUrn());
        if (t.getTag() == null) throw new RestException("Null Tag Object");
        a.setTagUrn(t.getTag().getUrn());
        if (t.getTimestamp() == null) throw new RestException("Null Timestamp Object");
        a.setDatetime(Constants.epochToISODatetime(t.getTimestamp()));
        return a;
    }

    public Annotation createOrUpdateTagging(Annotation annotation) {
        Tag tag = tagRepository.findByUrn(annotation.getTagUrn());
        if (tag == null) throw new RestException("Provide a valid tag urn");
        TagDomain td = tagDomainRepository.findByTag(tag.getUrn());
        Asset asset = assetRepository.findByUrn(annotation.getAssetUrn());
        if (asset == null) {
            //todo validate assetUrn
            asset = new Asset();
            asset.setUrn(annotation.getAssetUrn());
            asset = assetRepository.save(asset);
        }

        Tagging tagg = new Tagging();
        Boolean exists = false;
        Tagging taggToDelete = null;
        for (Tagging t : asset.getTaggings()) {
            TagDomain tdT = tagDomainRepository.findByTag(t.getTag().getUrn());
            if (t.getTag().getUrn().equals(tag.getUrn()) && t.getAsset().getUrn().equals(asset.getUrn()) && t.getApplication().equals(annotation.getApplication()) && t.getUser().equals(annotation.getUser())) {
                tagg = t;
                exists = true;
                break;
            }
            if (tdT.getUrn().equals(td.getUrn())) { //There is a tagging
                taggToDelete = t;
            }
        }
        if (taggToDelete != null) {
            asset.getTaggings().remove(taggToDelete);
        }
        Long timestamp = System.currentTimeMillis();
        if (exists == false) {
            asset.getTaggings().add(tagg);
        }
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
        tagg.setAsset(asset);
        tagg.setTag(tag);
        assetRepository.save(asset);
        return getAnnotation(tagg);
    }


    public List<Annotation> getAllAnnotations() {
        List<Annotation> annotations = new ArrayList<>();
        for (Asset asset : assetRepository.findAll()) {
            for (Tagging t : asset.getTaggings()) {
                annotations.add(getAnnotation(t));
            }
        }
        return annotations;
    }


    public List<Annotation> getAnnotationsOfAsset(String assetUrn) {
        if (assetUrn.equals("*")){
            return getAllAnnotations();
        }
        Asset asset = assetRepository.findByUrn(assetUrn);
        if (asset == null) {
            throw new RestException("AssetUrn Unknown");
        }
        List<Annotation> annotations = new ArrayList<>();
        for (Tagging t : asset.getTaggings()) {
            annotations.add(getAnnotation(t));
        }
        return annotations;
    }

    public Annotation getAnnotationForAssetApplicationUserTag(String assetUrn, String application, String user, String tagUrn) {
        Asset asset = assetRepository.findByUrn(assetUrn);
        if (asset == null) {
            throw new RestException("AssetUrn Unknown");
        }
        //todo add more

        for (Tagging t : asset.getTaggings()) {
            if (t.getUser().equals(user) && t.getApplication().equals(application) && t.getTag().getUrn().equals(tagUrn)) {
                return getAnnotation(t);
            }
        }
        throw new RestException("No Annotation Found");
    }

    public Annotation getAnnotationForAssetApplicationUserTagDomain(String assetUrn, String application, String user, String tagDomain) {
        Asset asset = assetRepository.findByUrn(assetUrn);
        if (asset == null) {
            throw new RestException("AssetUrn Unknown");
        }
        //todo add more
        for (Tagging t : asset.getTaggings()) {
            if (t.getUser().equals(user) && t.getApplication().equals(application)) {
                TagDomain td = tagDomainRepository.findByTag(t.getTag().getUrn());
                if (td.getUrn().equals(tagDomain))
                    return getAnnotation(t);
            }
        }
        throw new RestException("No Annotation Found");
    }

    public void deleteAssetsAndAnnotations(String assetUrn) {
        try {
            if (assetUrn.equals("*"))
                assetRepository.deleteAll();
            else {
                Asset asset = assetRepository.findByUrn(assetUrn);
                if (asset == null) throw new RestException("AssetUrn Unknown");
                assetRepository.delete(asset);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException("Delete All Asset/Annotations Failed" + e.getMessage());
        }
    }


}
