package eu.organicity.annotation.common.dto;

import java.util.Set;

public class AssetAnnotationListItemDTO {
    private String assetUrn;
    private long count;
    private Set<AnnotationDTO> annotations;

    public AssetAnnotationListItemDTO(String assetUrn, int count, Set<AnnotationDTO> annotations) {
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

    public Set<AnnotationDTO> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<AnnotationDTO> annotations) {
        this.annotations = annotations;
    }
}