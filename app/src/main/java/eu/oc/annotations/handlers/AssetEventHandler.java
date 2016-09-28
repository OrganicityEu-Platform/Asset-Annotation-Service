package eu.oc.annotations.handlers;

import eu.oc.annotations.domain.Asset;
import eu.oc.annotations.repositories.AssetRepository;
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
@RepositoryEventHandler(Asset.class)
public class AssetEventHandler {

    @Autowired
    AssetRepository assetRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetEventHandler.class);

    @HandleBeforeSave
    public void handleBeforeSave(Asset asset) throws RestException {
        // LOGGER.debug("Handling SpPtlCompanyProjects for: Company ID " + CurrentUserDetailsService.getCurrentUser().getUsername());
        LOGGER.debug("Handling Asset handleBeforeSave");
    }

    @HandleBeforeCreate
    public void handleBeforeCreates(Asset asset) throws RestException {
        LOGGER.debug("Handling Asset handleBeforeCreates");
        Asset a = assetRepository.findByUrn(asset.getUrn());
        if (a!=null) {
            throw new RestException("An Asset with the same Urn can not be created");
        }
    }
}

