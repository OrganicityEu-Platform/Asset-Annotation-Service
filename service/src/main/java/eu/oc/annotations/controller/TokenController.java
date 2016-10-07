package eu.oc.annotations.controller;

import eu.oc.annotations.config.OrganicityAccount;
import eu.oc.annotations.service.OrganicityUserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @RequestMapping(value = {"", "/", "token"}, method = RequestMethod.GET)
    public final String token() {

        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();

        return ou.toString();
    }


}
