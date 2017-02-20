package eu.organicity.annotation.common.dto;


import java.util.Set;

public class AssetListDTO {
    private Set<String> assetUrns;

    public Set<String> getAssetUrns() {
        return assetUrns;
    }

    public void setAssetUrns(Set<String> assetUrns) {
        this.assetUrns = assetUrns;
    }
}