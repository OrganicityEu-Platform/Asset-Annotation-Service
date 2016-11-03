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

    /**
     * Sets a property for a user on Mixpanel.
     *
     * @param userId        the unique id of the user.
     * @param property      a property for the user's profile.
     * @param propertyValue the value for the user's profile property.
     */
    public void setUserProperty(final String userId, final String property, final Object propertyValue) {
        if (mixpanel == null) {
            return;
        }

        final JSONObject props = new JSONObject();
        props.put(property, propertyValue);
        final JSONObject update = messageBuilder.set(userId, props);

        // Send the update to mixpanel
        try {
            mixpanel.sendMessage(update);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    /**
     * Add an event on Mixpanel for a user.
     *
     * @param principal the Principal of the User.
     * @param eventName the name for the event to add.
     */
    public void addEvent(Principal principal, final String eventName) {
        if (principal != null && principal.getName() != null) {
            addEvent(principal.getName(), eventName, null, null, null, null);
        }
    }

    /**
     * Add an event on Mixpanel for a user.
     *
     * @param principal     the Principal of the User.
     * @param eventName     the name for the event to add.
     * @param propertyName  a property for the event.
     * @param propertyValue the value for the event's property.
     */
    public void addEvent(Principal principal, final String eventName, final String propertyName, final Object propertyValue) {
        if (principal != null && principal.getName() != null) {
            addEvent(principal.getName(), eventName, propertyName, propertyValue, null, null);
        }
    }

    /**
     * Add an event on Mixpanel for a user.
     *
     * @param principal      the Principal of the User.
     * @param eventName      the name for the event to add.
     * @param propertyName   a property for the event.
     * @param propertyValue  the value for the event's property.
     * @param propertyName1  a second property for the event.
     * @param propertyValue1 the value for the event's second property.
     */
    public void addEvent(Principal principal, final String eventName, final String propertyName, final Object propertyValue, final String propertyName1, final Object propertyValue1) {
        if (mixpanel == null) {
            return;
        }

        if (principal == null || principal.getName() == null) {
            //addEvent("anon", eventName, propertyName, propertyValue, propertyName1, propertyValue1);
        } else {
            addEvent(principal.getName(), eventName, propertyName, propertyValue, propertyName1, propertyValue1);
        }
    }

    private void addEvent(final String userId, final String eventName
            , final String propertyName, final Object propertyValue
            , final String propertyName1, final Object propertyValue1) {
        if (mixpanel == null) {
            return;
        }
        setUserProperty(userId, "id", userId);

        // You can send properties along with events
        JSONObject props = new JSONObject();
        if (propertyName != null && propertyValue != null) {
            props.put(propertyName, propertyValue);
        }
        if (propertyName1 != null && propertyValue1 != null) {
            props.put(propertyName1, propertyValue1);
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
