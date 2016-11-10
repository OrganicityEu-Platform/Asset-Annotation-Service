package eu.oc.annotations.domain.dto;

import eu.oc.annotations.domain.Service;

import java.util.Set;

/**
 * Created by amaxilatis on 9/11/2016.
 */
public class TagDomainDto {
    private long id;
    private String urn;
    private String description;
    private Set<TagDto> tags;
    private Service[] services;

    public TagDomainDto() {
    }

    public TagDomainDto(long id, String urn, String description, Set<TagDto> tags) {
        this.id = id;
        this.urn = urn;
        this.description = description;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }

    public Service[] getServices() {
        return services;
    }

    public void setServices(Service[] services) {
        this.services = services;
    }
}
