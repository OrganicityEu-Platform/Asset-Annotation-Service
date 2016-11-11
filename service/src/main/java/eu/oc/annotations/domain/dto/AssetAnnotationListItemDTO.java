package eu.oc.annotations.domain.dto;

import eu.oc.annotations.domain.Annotation;

import java.util.Set;

public class AssetAnnotationListItemDTO {
    private String assetUrn;
    private long count;
    private Set<Annotation> annotations;

    public AssetAnnotationListItemDTO(String assetUrn, int count, Set<Annotation> annotations) {
        this.assetUrn = assetUrn;
        this.count = count;
        this.annotations = annotations;
    }

    public String getAssetUrn() {
        return assetUrn;
    }

    public void setAssetUrn(String assetUrn) {
        this.assetUrn = assetUrn;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }
}