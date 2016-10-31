package eu.oc.annotations.service;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Principal;

@Service
public class KPIService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KPIService.class);

    @Value("${mixpanel.token}")
    private String mixpanelToken;
    private MessageBuilder messageBuilder;
    private MixpanelAPI mixpanel;

    @PostConstruct
    public void init() {
        if (mixpanelToken.equals("")) {
            messageBuilder = null;
            mixpanel = null;
        } else {
            messageBuilder = new MessageBuilder(mixpanelToken);
            mixpanel = new MixpanelAPI();
        }

    }

    public void setUserProperty(final String userId, final String property, final Object properyValue) {
        if (mixpanel == null) {
            return;
        }

        final JSONObject props = new JSONObject();
        props.put(property, properyValue);
        final JSONObject update = messageBuilder.set(userId, props);

        // Send the update to mixpanel
        try {
            mixpanel.sendMessage(update);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public void addEvent(Principal principal, final String eventName, final String propertyName, final Object propertyValue) {
        if (mixpanel == null) {
            return;
        }

        if (principal == null || principal.getName() == null) {
            //addEvent("anon", eventName, propertyName, propertyValue, null, null);
        } else {
            addEvent(principal.getName(), eventName, propertyName, propertyValue, null, null);
        }
    }

    public void addEvent(Principal principal, final String eventName, final String propertyName, final Object propertyValue, final String propertyName1, final Object propertyValue1) {
        if (mixpanel == null) {
            return;
        }

        if (principal == null || principal.getName() == null) {
            //addEvent("anon", eventName, propertyName, propertyValue);
        } else {
            addEvent(principal.getName(), eventName, propertyName, propertyValue, propertyName1, propertyValue1);
        }
    }

    public void addEvent(Principal principal, final String eventName) {
        if (mixpanel == null) {
            return;
        }

        if (principal == null || principal.getName() == null) {
            //addEvent("anon", eventName,null,null,null,null);
        } else {
            addEvent(principal.getName(), eventName, null, null, null, null);
        }
    }

    private void addEvent(final String userId, final String eventName, final String properyName, final Object propertyValue, final String properyName1, final Object propertyValue1) {
        if (mixpanel == null) {
            return;
        }
        setUserProperty(userId, "id", userId);

        // You can send properties along with events
        JSONObject props = new JSONObject();
        if (properyName != null && propertyValue != null) {
            props.put(properyName, propertyValue);
        }
        if (properyName1 != null && propertyValue1 != null) {
            props.put(properyName1, propertyValue1);
        }

        final JSONObject event = messageBuilder.event(userId, eventName, props);
        final ClientDelivery delivery = new ClientDelivery();
        delivery.addMessage(event);

        try {
            mixpanel.deliver(delivery);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
