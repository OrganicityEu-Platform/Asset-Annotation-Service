package eu.oc.annotations.service;

import eu.oc.annotations.domain.Application;
import eu.oc.annotations.domain.Tag;
import eu.oc.annotations.domain.TagDomain;
import eu.organicity.annotation.service.dto.ExperimentDTO;
import eu.organicity.annotation.service.dto.TagDTO;
import eu.organicity.annotation.service.dto.TagDomainDTO;
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
        if (tagDomain.getTags() != null) {
            for (Tag tag : tagDomain.getTags()) {
                dto.getTags().add(toDTO(tag));
            }
        }
        return dto;
    }

    public TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setUrn(tag.getUrn());
        tagDTO.setName(tag.getName());
        return tagDTO;
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


    public Set<TagDTO> toTagSetDTO(Set<Tag> tags) {
        Set<TagDTO> dto = new HashSet<>();
        for (Tag tag : tags) {
            dto.add(toDTO(tag));
        }
        return dto;
    }
}
