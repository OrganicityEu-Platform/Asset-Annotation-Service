package eu.oc.annotations.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TokenController {

    //Get All tagDomains
    @RequestMapping(value = {"token"}, method = RequestMethod.GET)
    public final String domainFindAll(Principal principal) {
         return principal.getName();
    }


}
