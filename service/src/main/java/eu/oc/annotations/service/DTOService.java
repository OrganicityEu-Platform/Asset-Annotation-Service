package eu.oc.annotations.service;

import eu.oc.annotations.domain.*;
import eu.organicity.annotation.service.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by amaxilat on 1/12/2016.
 */
@Service
public class DTOService {

    public ExperimentDTO toDTO(Application experiment) {
        ExperimentDTO dto = new ExperimentDTO();
        dto.setId(experiment.getId());
        dto.setUrn(experiment.getUrn());
        dto.setDescription(experiment.getDescription());
        dto.setTagDomains(new ArrayList<>());
        dto.setUser(experiment.getUser());
        if (experiment.getTagDomains() != null) {
            for (TagDomain tagDomain : experiment.getTagDomains()) {
                dto.getTagDomains().add(toDTO(tagDomain));
            }
        }
        return dto;
    }

    public TagDomainDTO toDTO(TagDomain tagDomain) {
        TagDomainDTO dto = new TagDomainDTO();
        dto.setId(tagDomain.getId());
        dto.setUrn(tagDomain.getUrn());
        dto.setDescription(tagDomain.getDescription());
        dto.setTags(new HashSet<>());
        dto.setUser(tagDomain.getUser());
        if (tagDomain.getTags() != null) {
            for (Tag tag : tagDomain.getTags()) {
                dto.getTags().add(toDTO(tag));
            }
        }
        dto.setServices(new HashSet<>());
        if (tagDomain.getServices() != null) {
            for (eu.oc.annotations.domain.Service service : tagDomain.getServices()) {
                dto.getServices().add(toDTO(service));
            }
        }
        return dto;
    }

    public TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setUrn(tag.getUrn());
        tagDTO.setName(tag.getName());
        tagDTO.setUser(tag.getUser());
        return tagDTO;
    }

    public ServiceDTO toDTO(eu.oc.annotations.domain.Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setUrn(service.getUrn());
        dto.setDescription(service.getDescription());
        dto.setUser(service.getUser());
        return dto;
    }

    public List<ExperimentDTO> toExperimentListDTO(List<Application> applications) {
        ArrayList<ExperimentDTO> dtoList = new ArrayList<>();
        for (Application application : applications) {
            dtoList.add(toDTO(application));
        }
        return dtoList;
    }

    public List<TagDomainDTO> toTagDomainListDTO(List<TagDomain> tagDomains) {
        final List<TagDomainDTO> list = new ArrayList<>();
        for (final TagDomain tagDomain : tagDomains) {
            list.add(toDTO(tagDomain));
        }
        return list;
    }

    public List<ServiceDTO> toServiceListDTO(List<eu.oc.annotations.domain.Service> services) {
        final List<ServiceDTO> list = new ArrayList<>();
        for (final eu.oc.annotations.domain.Service service : services) {
            list.add(toDTO(service));
        }
        return list;
    }

    public Set<TagDTO> toTagSetDTO(Set<Tag> tags) {
        Set<TagDTO> dto = new HashSet<>();
        for (Tag tag : tags) {
            dto.add(toDTO(tag));
        }
        return dto;
    }

    public Set<AnnotationDTO> toAssetListDTO(List<Asset> all) {
        final HashSet<AnnotationDTO> dto = new HashSet<>();
        for (final Asset asset : all) {
            for (final Tagging tagging : asset.getTaggings()) {
                dto.add(toDTO(tagging));
            }
        }
        return dto;
    }

    public AnnotationDTO toDTO(final Tagging tagging) {
        AnnotationDTO dto = new AnnotationDTO();
        dto.setAnnotationId(tagging.getTaggingId());
        dto.setApplication(tagging.getApplication());
        dto.setAssetUrn(tagging.getAsset().getUrn());
        dto.setDatetime("" + tagging.getTimestamp());
        dto.setUser(tagging.getUser());
        dto.setTagUrn(tagging.getTag().getUrn());
        dto.setTextValue(tagging.getTextValue());
        dto.setNumericValue(tagging.getNumericValue());
        return dto;
    }

    public AnnotationDTO toAnnotationDTO(final Annotation tagging) {
        AnnotationDTO dto = new AnnotationDTO();
        dto.setAnnotationId(tagging.getAnnotationId());
        dto.setApplication(tagging.getApplication());
        dto.setAssetUrn(tagging.getAssetUrn());
        dto.setDatetime(tagging.getDatetime());
        dto.setUser(tagging.getUser());
        dto.setTagUrn(tagging.getTagUrn());
        dto.setTextValue(tagging.getTextValue());
        dto.setNumericValue(tagging.getNumericValue());
        return dto;
    }
}
