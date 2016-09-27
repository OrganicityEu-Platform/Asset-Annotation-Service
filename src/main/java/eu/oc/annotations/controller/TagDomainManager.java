package eu.oc.annotations.controller;

import eu.oc.annotations.domain.Application;
import eu.oc.annotations.domain.Service;
import eu.oc.annotations.domain.Tag;
import eu.oc.annotations.domain.TagDomain;
import eu.oc.annotations.handlers.RestException;
import eu.oc.annotations.repositories.ApplicationRepository;
import eu.oc.annotations.repositories.ServiceRepository;
import eu.oc.annotations.repositories.TagDomainRepository;
import eu.oc.annotations.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagDomainManager {

    @Autowired
    TagDomainRepository tagDomainRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    // TAG DOMAIN METHODS--------------------------------------------------------------------------------

    //Create tagDomain
    @RequestMapping(value = {"admin/tagDomains"}, method = RequestMethod.POST)
    public final TagDomain domainCreate(@RequestBody TagDomain domain) {
        if (domain.getId() != null) {
            throw new RestException("TagDomain Exception: TagDomain.id has to be null");
        }
        TagDomain a = tagDomainRepository.findByUrn(domain.getUrn());
        if (a != null) { //tagDomain Create
            throw new RestException("TagDomain Exception: duplicate urn");
        }
        domain.setId(null);
        try {
            domain = tagDomainRepository.save(domain);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return domain;
    }

    //Update tagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}"}, method = RequestMethod.POST)
    public final TagDomain domainUpdate(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody TagDomain domain) {
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        d.setDescription(domain.getDescription());
        try {
            domain = tagDomainRepository.save(d);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return domain;
    }

    //Delete tagDomain
    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}"}, method = RequestMethod.DELETE)
    public final void domainDelete(@PathVariable("tagDomainUrn") String tagDomainUrn) {
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        if (d.getTags()!=null && d.getTags().size() > 0) {
            throw new RestException("TagDomain is not empty");
        }
        try {
            tagDomainRepository.delete(d);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }


    // TAG METHODS-----------------------------------------------------------------------------------------------------

    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.POST)
    public final Tag domainCreateTag(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody Tag tag) {
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        Tag a = tagRepository.findByUrn(tag.getUrn());
        if (a != null) {
            throw new RestException("Tag:Duplicate Urn");
        }
        tag.setId(null);
        d.getTags().add(tag);
        try {
            tagDomainRepository.save(d);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return tag;
    }

    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/tags"}, method = RequestMethod.DELETE)
    public final void domainRemoveTag(@PathVariable("tagDomainUrn") String tagDomainUrn, @RequestBody String tagUrn) {
        TagDomain d = tagDomainRepository.findByUrn(tagDomainUrn);
        if (d == null) {
            throw new RestException("TagDomain Not Found");
        }
        tagUrn = tagUrn.replace("\"", "");
        Tag t = tagRepository.findByUrn(tagUrn);
        if (t == null) {
            throw new RestException("Tag Not Found");
        }
        if (d.containsTag(tagUrn) == false) {
            throw new RestException("TagDomain/Tag are not associated");
        }
        //todo check if there are taggings
        try {
            tagRepository.delete(t.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }

    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.GET)
    public final List<Service> tagDomainGetServices(@PathVariable("tagDomainUrn") String tagDomainUrn) {
        TagDomain a = tagDomainRepository.findByUrn(tagDomainUrn);
        if (a == null) {
            throw new RestException("TagDomain Not Found");
        }
        return a.getServices();
    }

    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.POST)
    public final TagDomain serviceAddTagDomains(@RequestParam(value="serviceUrn", required=true) String serviceUrn, @PathVariable("tagDomainUrn") String tagDomainUrn) {
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        TagDomain a = tagDomainRepository.findByUrn(tagDomainUrn);
        if (a == null) {
            throw new RestException("TagDomain Not Found");
        }
        try {
            a.getServices().add(s);
            return tagDomainRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }

    @RequestMapping(value = {"admin/tagDomains/{tagDomainUrn}/services"}, method = RequestMethod.DELETE)
    public final void serviceRemoveTagDomains(@RequestParam(value="serviceUrn", required=true) String serviceUrn, @PathVariable("tagDomainUrn") String tagDomainUrn) {
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        TagDomain a = tagDomainRepository.findByUrn(tagDomainUrn);
        if (a == null) {
            throw new RestException("TagDomain Not Found");
        }

        try {
            a.getServices().remove(s);
            tagDomainRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }


    // Services METHODS-----------------------------------------------

    //Create Service
    @RequestMapping(value = {"admin/services"}, method = RequestMethod.POST)
    public final Service servicesCreate(@RequestBody Service service) {
        if (service.getId() != null) {
            throw new RestException("Service Exception: Service.id has to be null");
        }
        Service a = serviceRepository.findByUrn(service.getUrn());
        if (a != null) { //tagDomain Create
            throw new RestException("Service Exception: duplicate urn");
        }
        service.setId(null);
        try {
            service = serviceRepository.save(service);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return service;
    }

    //Delete Service
    @RequestMapping(value = {"admin/services/{serviceUrn}"}, method = RequestMethod.DELETE)
    public final void serviceDelete(@PathVariable("serviceUrn") String serviceUrn) {
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        try {
            serviceRepository.delete(s);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }

    @RequestMapping(value = {"admin/services/{serviceUrn}/tagDomains"}, method = RequestMethod.GET) //todo
    public final List<TagDomain> serviceGetTagDomains(@PathVariable("serviceUrn") String serviceUrn) {
        Service s = serviceRepository.findByUrn(serviceUrn);
        if (s == null) {
            throw new RestException("Service Not Found");
        }
        List<TagDomain> asnwer = tagDomainRepository.findAllByService(s.getUrn());;
        return asnwer;
    }

    // Application METHODS-----------------------------------------------

    //Create Service
    @RequestMapping(value = {"admin/applications"}, method = RequestMethod.POST)
    public final Application applicationsCreate(@RequestBody Application application) {
        if (application.getId() != null) {
            throw new RestException("Application Exception: Application.id has to be null");
        }
        Application a = applicationRepository.findByUrn(application.getUrn());
        if (a != null) { //tagDomain Create
            throw new RestException("Application Exception: duplicate urn");
        }
        application.setId(null);
        try {
            application = applicationRepository.save(application);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return application;
    }

    //Delete Service
    @RequestMapping(value = {"admin/applications/{applicationUrn}"}, method = RequestMethod.DELETE)
    public final void applicationDelete(@PathVariable("applicationUrn") String applicationUrn) {
        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        try {
            applicationRepository.delete(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }

    @RequestMapping(value = {"admin/applications/{applicationUrn}/tagDomains"}, method = RequestMethod.GET) //todo
    public final List<TagDomain> applicationGetTagDomains(@PathVariable("applicationUrn") String applicationUrn) {
        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        List<TagDomain> asnwer = a.getTagDomains();
        return asnwer;
    }


    @RequestMapping(value = {"admin/applications/{applicationUrn}/tagDomains"}, method = RequestMethod.POST)
    public final Application applicationAddTagDomains(@RequestParam(value="tagDomainUrn", required=true) String tagDomainUrn, @PathVariable("applicationUrn") String applicationUrn) {
        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        TagDomain td = tagDomainRepository.findByUrn(tagDomainUrn);
        if (td == null) {
            throw new RestException("TagDomain Not Found");
        }
        try {
            a.getTagDomains().add(td);
            a=applicationRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
        return a;
    }

    @RequestMapping(value = {"admin/applications/{applicationUrn}/tagDomains"}, method = RequestMethod.DELETE)
    public final void applicationRemoveTagDomains(@RequestParam(value="tagDomainUrn", required=true) String tagDomainUrn, @PathVariable("applicationUrn") String applicationUrn) {
        Application a = applicationRepository.findByUrn(applicationUrn);
        if (a == null) {
            throw new RestException("Application Not Found");
        }
        TagDomain td = tagDomainRepository.findByUrn(tagDomainUrn);
        if (td == null) {
            throw new RestException("TagDomain Not Found");
        }

        try {
            a.getTagDomains().remove(td);
            applicationRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage());
        }
    }




}
