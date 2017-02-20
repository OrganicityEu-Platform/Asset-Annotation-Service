package eu.oc.annotations.service;

import eu.oc.annotations.domain.Annotation;
import eu.oc.annotations.domain.Application;
import eu.oc.annotations.domain.Tag;
import eu.oc.annotations.domain.TagDomain;
import eu.oc.annotations.domain.TagDomainService;
import eu.oc.annotations.domain.Tagging;
import eu.oc.annotations.repositories.TagDomainServiceRepository;
import eu.organicity.annotation.service.dto.AnnotationDTO;
import eu.organicity.annotation.service.dto.ExperimentDTO;
import eu.organicity.annotation.service.dto.ServiceDTO;
import eu.organicity.annotation.service.dto.TagDTO;
import eu.organicity.annotation.service.dto.TagDomainDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    TagDomainServiceRepository tagDomainServiceRepository;

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
        List<TagDomainService> services = tagDomainServiceRepository.findByTagDomain(tagDomain);
        if (services != null) {
            for (TagDomainService service : services) {
                dto.getServices().add(toDTO(service.getService()));
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

    public List<ExperimentDTO> toExperimentListDTO(Iterable<Application> applications) {
        ArrayList<ExperimentDTO> dtoList = new ArrayList<>();
        for (Application application : applications) {
            dtoList.add(toDTO(application));
        }
        return dtoList;
    }

    public List<TagDomainDTO> toTagDomainListDTO(Iterable<TagDomain> tagDomains) {
        final List<TagDomainDTO> list = new ArrayList<>();
        for (final TagDomain tagDomain : tagDomains) {
            list.add(toDTO(tagDomain));
        }
        return list;
    }

    public List<TagDomainDTO> toTagDomainListDTOFromTagDomainServices(Iterable<TagDomainService> tagDomainServices) {
        final List<TagDomainDTO> list = new ArrayList<>();
        for (final TagDomainService tagDomainService : tagDomainServices) {
            list.add(toDTO(tagDomainService.getTagDomain()));
        }
        return list;
    }

    public List<ServiceDTO> toServiceListDTO(Iterable<TagDomainService> services) {
        final List<ServiceDTO> list = new ArrayList<>();
        for (final TagDomainService service : services) {
            list.add(toDTO(service.getService()));
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

    public Set<AnnotationDTO> toAssetListDTO(Iterable<Tagging> all) {
        final HashSet<AnnotationDTO> dto = new HashSet<>();
        for (final Tagging tagging : all) {
            dto.add(toDTO(tagging));
        }
        return dto;
    }

    public AnnotationDTO toDTO(final Tagging tagging) {
        AnnotationDTO dto = new AnnotationDTO();
        dto.setAnnotationId(tagging.getId());
        dto.setApplication(tagging.getApplication());
        dto.setAssetUrn(tagging.getUrn());
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
