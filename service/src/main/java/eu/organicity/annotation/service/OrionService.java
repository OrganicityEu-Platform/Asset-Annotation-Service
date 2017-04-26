package eu.organicity.annotation.service;

import eu.organicity.annotation.domain.Tagging;
import eu.organicity.annotation.repositories.TaggingRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrionService.class);
    
    @PostConstruct
    public void init() {
        for (Tagging tagging : taggingRepository.findAll()) {
            updateAnnotations(tagging.getUrn());
        }
    }
    
    @Autowired
    TaggingRepository taggingRepository;
    
    @Async
    public void updateAnnotations(final String assetUrn) {
        
        final Set<String> allTags = new HashSet<>();
        final List<Tagging> taggings = taggingRepository.findByUrn(assetUrn);
        for (final Tagging tagging : taggings) {
            allTags.add(tagging.getTag().getUrn());
        }
        
        final String tagsString = StringUtils.join(allTags, ",");
        
        LOGGER.info("[" + assetUrn + "] " + tagsString);
        
    }
}
