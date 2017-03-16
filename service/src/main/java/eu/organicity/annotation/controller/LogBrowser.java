package eu.organicity.annotation.controller;

import eu.organicity.annotation.domain.accounting.AccountingEntry;
import eu.organicity.annotation.service.AccountingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogBrowser {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogBrowser.class);

    @Autowired
    AccountingService accountingService;


    // TAG DOMAIN METHODS-----------------------------------------------

    //Get All tagDomains
    @RequestMapping(value = {"logs"}, method = RequestMethod.GET)
    public final List<AccountingEntry> list() {
        return accountingService.list();
    }

}
