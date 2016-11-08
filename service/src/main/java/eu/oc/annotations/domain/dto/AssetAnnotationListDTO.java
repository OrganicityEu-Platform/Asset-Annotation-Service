package eu.oc.annotations.domain.dto;

import java.util.Set;

public class AssetAnnotationListDTO {
    private Set<AssetAnnotationListItemDTO> assetAnnotations;

    public Set<AssetAnnotationListItemDTO> getAssetAnnotations() {
        return assetAnnotations;
    }

    public void setAssetAnnotations(Set<AssetAnnotationListItemDTO> assetAnnotations) {
        this.assetAnnotations = assetAnnotations;
    }
}