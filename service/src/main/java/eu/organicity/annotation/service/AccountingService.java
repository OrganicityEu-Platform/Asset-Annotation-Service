package eu.organicity.annotation.service;

import eu.organicity.annotation.domain.accounting.AccountingEntry;
import eu.organicity.annotation.domain.accounting.Context;
import eu.organicity.annotation.mongo.repository.AccountingEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class AccountingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountingService.class);
    private static final String SERVICE_NAME = "oc:annotations";
    public static final String READ_ACTION = "read";
    public static final String CREATE_ACTION = "create";
    public static final String UPDATE_ACTION = "update";
    public static final String DELETE_ACTION = "delete";

    private SimpleDateFormat dateFormatSeconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    AccountingEntryRepository accountingEntryRepository;

    @PostConstruct
    public void init() {
        List<AccountingEntry> acc = accountingEntryRepository.findAll();
        for (AccountingEntry accountingEntry : acc) {
            LOGGER.info(accountingEntry.toString());
        }

        final TimeZone tz = TimeZone.getTimeZone("UTC");
        dateFormatSeconds.setTimeZone(tz);
    }

    /**
     * Add an event on the OC Accounting Service.
     *
     * @param principal the Principal of the User.
     * @param eventName the name for the event to add.
     */
    @Async
    public void addMethod(Principal principal, final String action, final String eventName) {
        addMethod(principal, action, eventName, null, null, "200");
    }

    /**
     * Add an event on the OC Accounting Service.
     *
     * @param principal the Principal of the User.
     * @param eventName the name for the event to add.
     */
    @Async
    public void addMethod(Principal principal, final String action, final String eventName, final String urn, final String url) {
        addMethod(principal, action, eventName, urn, url, "200");
    }

    /**
     * Add an event on the OC Accounting Service.
     *
     * @param principal the Principal of the User.
     * @param eventName the name for the event to add.
     */
    @Async
    public void addMethod(Principal principal, final String action, final String eventName, final String urn, final String url, final String status) {
        LOGGER.info("addMethod " + principal + " event: " + eventName);

        final Context context = new Context();
        context.setMethod(eventName);
        context.setUrn(urn);
        context.setUrl(url);
        context.setStatus(status);

        final AccountingEntry entry = new AccountingEntry();
        entry.setSub(principal != null ? principal.getName() : "null");
        entry.setService(SERVICE_NAME);
        entry.setAction(action);
        entry.setTimestamp(dateFormatSeconds.format(new Date()));
        entry.setContext(context);

        accountingEntryRepository.save(entry);
    }

    public List<AccountingEntry> list() {
        return accountingEntryRepository.findAll();
    }
}
