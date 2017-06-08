package eu.organicity.annotation.service;

import eu.organicity.annotation.domain.Tagging;
import eu.organicity.annotation.repositories.TaggingRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrionService.class);
    
    @Autowired
    TaggingRepository taggingRepository;
    private RestTemplate restClient;
    
    @PostConstruct
    public void init() {
        restClient = new RestTemplate();
    }
    
    @Async
    public void updateAnnotations(final String assetUrn) {
        
        final Set<String> allTags = new HashSet<>();
        final List<Tagging> taggings = taggingRepository.findByUrn(assetUrn);
        for (final Tagging tagging : taggings) {
            allTags.add(tagging.getTag().getUrn());
            allTags.add(tagging.getTag().getName());
        }
        
        final String tagsString = StringUtils.join(allTags, ",");
        
        LOGGER.info("[" + assetUrn + "] " + tagsString);
        try {
            postAnnotationsToOrion(assetUrn, tagsString);
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        
    }
    
    private void postAnnotationsToOrion(String assetUrn, String tagsString) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Fiware-Service", "organicity");
        headers.add("Fiware-ServicePath", "/");
        HttpEntity<String> request = new HttpEntity<>("{\"annotations\":{\"value\":\"" + tagsString + "\",\"type\":\"urn:oc:attributeType:annotations\"}}", headers);
        restClient.postForEntity("http://localhost:1026/v2/entities/" + assetUrn + "/attrs", request, String.class);
    }
    
}
