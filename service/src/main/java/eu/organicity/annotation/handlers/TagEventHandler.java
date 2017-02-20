package eu.organicity.annotation.handlers;

import eu.organicity.annotation.domain.Tag;
import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.repositories.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

/**
 * Created by Annita on 5/20/2015.
 */

@Service
@RepositoryEventHandler(Tag.class)
public class TagEventHandler {

    @Autowired
    TagRepository tagRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagEventHandler.class);

    @HandleBeforeSave
    public void handleBeforeSave(TagDomain tagDomain) throws RestException {
        LOGGER.debug("Handling Tag handleBeforeSave");
    }

    @HandleBeforeCreate
    public void handleBeforeCreates(Tag tag) throws RestException {
        LOGGER.debug("Handling Tag handleBeforeCreates");
        Tag a = tagRepository.findByUrn(tag.getUrn());
        if (a !=null) {
            throw new RestException("An Tag with the same Urn can not be created");
        }
    }
}

