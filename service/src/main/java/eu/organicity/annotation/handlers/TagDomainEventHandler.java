package eu.organicity.annotation.handlers;

import eu.organicity.annotation.domain.TagDomain;
import eu.organicity.annotation.repositories.TagDomainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

/**
 * Created by etheodor on 5/20/2015.
 */

@Service
@RepositoryEventHandler(TagDomain.class)
public class TagDomainEventHandler {

    @Autowired
    TagDomainRepository tagDomainRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagDomainEventHandler.class);

    @HandleBeforeSave
    public void handleBeforeSave(TagDomain tagDomain) throws RestException {
        LOGGER.debug("Handling TagDomain handleBeforeSave");
    }

    @HandleBeforeCreate
    public void handleBeforeCreates(TagDomain tagDomain) throws RestException {
        LOGGER.debug("Handling TagDomain handleBeforeCreates");
        TagDomain a = tagDomainRepository.findByUrn(tagDomain.getUrn());
        if (a!=null) {
            throw new RestException("An TagDomain with the same Urn can not be created");
        }
    }
}

