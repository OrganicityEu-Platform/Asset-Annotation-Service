package eu.organicity.annotation.android.client;

import eu.organicity.annotation.common.dto.AnnotationDTO;
import eu.organicity.annotation.common.dto.ExperimentDTO;
import eu.organicity.annotation.common.dto.ServiceDTO;
import eu.organicity.annotation.common.dto.TagDTO;
import eu.organicity.annotation.common.dto.TagDomainDTO;
import eu.organicity.client.OrganicityServiceBaseClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

/**
 * Helper Client implementation for the OrganiCity Annotation Service.
 * This client implements the API described here:
 * https://annotations.organicity.eu/swagger-ui.html
 *
 * @author amaxilat@cti.gr
 */
public class AnnotationServiceAndroidClient extends OrganicityServiceBaseClient {
    private String baseUrl = "https://annotations.organicity.eu/";

    public AnnotationServiceAndroidClient(final String token) {
        super(token);
    }

    public AnnotationServiceAndroidClient(final String token, final String baseUrl) {
        super(token);
        this.baseUrl = baseUrl;
    }

    //Get Methods

    /**
     * List all available {@link TagDomainDTO}
     *
     * @return an array of {@link TagDomainDTO} objects
     */
    public TagDomainDTO[] getTagDomains() {
        return restTemplate.getForObject(baseUrl + "tagDomains", TagDomainDTO[].class);
    }

    public TagDomainDTO[] getTagDomainsOfExperiment(String experimentUrn) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "experiments/" + experimentUrn + "/tagDomains", HttpMethod.GET, entity, TagDomainDTO[].class).getBody();
    }

    /**
     * Retrieves all information for a given {@link TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@link TagDomainDTO}
     * @return the {@link TagDomainDTO}
     */
    public TagDomainDTO getTagDomain(final String tagDomainUrn) {
        return restTemplate.getForObject(baseUrl + "tagDomains/" + tagDomainUrn, TagDomainDTO.class);
    }

    /**
     * Lists all {@link TagDTO} elements of a {@link TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@link TagDomainDTO}
     * @return an array of {@link TagDTO}
     */
    public TagDTO[] getTags(final String tagDomainUrn) {
        return restTemplate.getForObject(baseUrl + "tagDomains/" + tagDomainUrn + "/tags", TagDTO[].class);
    }

    /**
     * List all {@link ServiceDTO} of a {@link TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@link TagDomainDTO}
     * @return an array of {@link ServiceDTO}
     */
    public ServiceDTO[] tagDomainGetServices(final String tagDomainUrn) {
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/services", HttpMethod.GET, entity, ServiceDTO[].class).getBody();
    }

    /**
     * List all {@link TagDomainDTO} of a {@link ExperimentDTO}
     *
     * @param experimentUrn the urn of the {@link ExperimentDTO}
     * @return an array of {@link TagDomainDTO}
     */
    public TagDomainDTO[] experimentGetTagDomains(final String experimentUrn) {
        return restTemplate.getForObject(baseUrl + "admin/experiments/" + experimentUrn + "/tagDomains", TagDomainDTO[].class);
    }

    //Add Methods

    /**
     * Adds an {@link AnnotationDTO}
     *
     * @param annotationDTO the {@link AnnotationDTO} to add
     * @return the added {@link AnnotationDTO}
     */
    public AnnotationDTO postAnnotation(final AnnotationDTO annotationDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnnotationDTO> entity = new HttpEntity<>(annotationDTO, headers);
        return restTemplate.exchange(baseUrl + "annotations/" + annotationDTO.getAssetUrn(), HttpMethod.POST, entity, AnnotationDTO.class).getBody();
    }

    /**
     * Adds a {@link TagDomainDTO}
     *
     * @param urn         the {@link TagDomainDTO} urn
     * @param description a text description  for the {@link TagDomainDTO}
     * @return the added {@link TagDomainDTO}
     */
    public TagDomainDTO addTagDomain(final String urn, final String description) {
        final TagDomainDTO dto = new TagDomainDTO();
        dto.setUrn(urn);
        dto.setDescription(description);
        return postTagDomain(dto);
    }

    /**
     * Adds a {@link TagDomainDTO}
     *
     * @param urn         the {@link TagDomainDTO} urn
     * @param description a text description  for the {@link TagDomainDTO}
     * @return the added {@link TagDomainDTO}
     */
    public TagDomainDTO addTagDomain(final String urn, final String description, final Set<TagDTO> tags) {
        final TagDomainDTO dto = new TagDomainDTO();
        dto.setUrn(urn);
        dto.setDescription(description);
        dto.setTags(tags);
        return postTagDomain(dto);
    }

    /**
     * Adds a {@link TagDomainDTO}
     *
     * @param urn         the {@link TagDomainDTO} urn
     * @param description a text description  for the {@link TagDomainDTO}
     * @return the added {@link TagDomainDTO}
     */
    public ExperimentDTO addExperiment(final String urn, final String description) {
        final ExperimentDTO dto = new ExperimentDTO();
        dto.setUrn(urn);
        dto.setDescription(description);
        return postExperiment(dto);
    }

    /**
     * Adds a {@link TagDTO} to a {@link TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@link TagDomainDTO}
     * @param urn          the urn of the {@link TagDTO} to add
     * @param name         a text name for the {@link TagDTO}
     * @return the added {@link TagDTO}
     */
    public TagDTO addTag(final String tagDomainUrn, final String urn, final String name) {
        final TagDTO dto = new TagDTO();
        dto.setUrn(urn);
        dto.setName(name);
        return postTag2TagDomain(tagDomainUrn, dto);
    }

    /**
     * Creates a new {@link ServiceDTO}
     *
     * @param serviceDTO the {@link ServiceDTO} to add
     * @return the added {@link ServiceDTO}
     */
    public ServiceDTO servicesCreate(final ServiceDTO serviceDTO) {
        HttpEntity<ServiceDTO> entity = new HttpEntity<>(serviceDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/services", HttpMethod.POST, entity, ServiceDTO.class).getBody();
    }

    public ServiceDTO servicesCreate(final String urn) {
        ServiceDTO dto = new ServiceDTO();
        dto.setUrn(urn);
        dto.setDescription(urn);
        return servicesCreate(dto);
    }

    /**
     * Adds a {@link ServiceDTO} to a {@link TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@link TagDomainDTO}
     * @param serviceUrn   the urn of the {@link ServiceDTO}
     * @return the {@link TagDomainDTO}
     */
    public TagDomainDTO serviceAddTagDomains(final String tagDomainUrn, final String serviceUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/services");
        if (serviceUrn != null) {
            builder = builder.queryParam("serviceUrn", serviceUrn);
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.postForObject(builder.build().encode().toUri(), entity, TagDomainDTO.class);
    }

    /**
     * Adds a new {@link ExperimentDTO}
     *
     * @param experimentDTO the urn of the experiment to add
     * @return the added {@link ExperimentDTO}
     */
    public ExperimentDTO experimentCreate(final String experimentUrn) {
        ExperimentDTO dto = new ExperimentDTO();
        dto.setUrn(experimentUrn);
        return experimentCreate(dto);
    }

    /**
     * Adds a new {@link ExperimentDTO}
     *
     * @param experimentDTO the {@link ExperimentDTO} to add
     * @return the added {@link ExperimentDTO}
     */
    public ExperimentDTO experimentCreate(final ExperimentDTO experimentDTO) {
        HttpEntity<ExperimentDTO> entity = new HttpEntity<>(experimentDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/experiments", HttpMethod.POST, entity, ExperimentDTO.class).getBody();
    }

    /**
     * Adds new {@link ExperimentDTO}
     *
     * @param tagDomainUrn  the urn of the {@link TagDomainDTO}
     * @param experimentUrn the urn of the {@link ExperimentDTO}
     * @return the updated {@link ExperimentDTO}
     */
    public ExperimentDTO experimentAddTagDomains(final String tagDomainUrn, final String experimentUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "admin/experiments/" + experimentUrn + "/tagDomains");
        if (tagDomainUrn != null) {
            builder = builder.queryParam("tagDomainUrn", tagDomainUrn);
        }
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.postForObject(builder.build().encode().toUri(), entity, ExperimentDTO.class);
    }

    //Remove Methods

    /**
     * Deletes a {@link TagDomainDTO} and all the {@link TagDTO} it contains.
     *
     * @param tagDomainUrn the urn of the {@link TagDomainDTO}
     */
    public void removeTagDomain(final String tagDomainUrn) {
        final TagDomainDTO domain = getTagDomain(tagDomainUrn);

        if (domain == null) {
            return;
        }

        deleteTagDomain(tagDomainUrn);
    }

    /**
     * Removes a {@link TagDTO} from a {@link TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@link TagDomainDTO}
     * @param tagUrn       the urn of the {@link TagDTO}
     */
    public void removeTag(final String tagDomainUrn, final String tagUrn) {
        HttpEntity<String> entity = new HttpEntity<>(tagUrn, headers);
        restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/tags", HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Deletes a {@link ServiceDTO}
     *
     * @param serviceUrn the urn of the {@link ServiceDTO}
     */
    public void serviceDelete(final String serviceUrn) {
        HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(baseUrl + "admin/services/" + serviceUrn, HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Removes a {@link TagDomainDTO} from a {@link ServiceDTO}
     *
     * @param tagDomainUrn the urn of the {@link TagDomainDTO}
     * @param serviceUrn   the urn of the {@link ServiceDTO}
     */
    public void serviceRemoveTagDomains(final String tagDomainUrn, final String serviceUrn) {
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/services");
//        if (serviceUrn != null) {
//            builder = builder.queryParam("serviceUrn", serviceUrn);
//        }

        HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/services?serviceUrn=" + serviceUrn, HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Deletes an {@link ExperimentDTO}
     *
     * @param applicationUrn the urn of the {@link ExperimentDTO}
     */
    public void experimentDelete(final String applicationUrn) {
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(baseUrl + "admin/applications/" + applicationUrn, HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Removes a {@link TagDomainDTO} from the {@link ExperimentDTO}
     *
     * @param tagDomainUrn  the urn of the {@link TagDomainDTO}
     * @param experimentUrn the urn of the {@link ExperimentDTO}
     */
    public void experimentRemoveTagDomains(final String tagDomainUrn, final String experimentUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "admin/experiments/" + experimentUrn + "/tagDomains");
        if (tagDomainUrn != null) {
            builder = builder.queryParam("tagDomainUrn", tagDomainUrn);
        }
        restTemplate.delete(builder.build().encode().toUri());
    }

    //internal calls

    private TagDomainDTO postTagDomain(final TagDomainDTO tagDomainDTO) {
        HttpEntity<TagDomainDTO> entity = new HttpEntity<>(tagDomainDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/tagDomains", HttpMethod.POST, entity, TagDomainDTO.class).getBody();
    }

    private ExperimentDTO postExperiment(final ExperimentDTO experimentDTO) {
        HttpEntity<ExperimentDTO> entity = new HttpEntity<>(experimentDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/experiments", HttpMethod.POST, entity, ExperimentDTO.class).getBody();
    }

    private void deleteTagDomain(final String tagDomainUrn) {
        //https://annotations.organicity.eu/admin/tagDomains/urn:oc:tagDomain:AnomalyDetection:0
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/", HttpMethod.DELETE, entity, String.class);
    }

    private TagDTO postTag2TagDomain(final String tagDomain, final TagDTO tagDTO) {
        HttpEntity<TagDTO> entity = new HttpEntity<>(tagDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomain + "/tag", HttpMethod.POST, entity, TagDTO.class).getBody();
    }


}
