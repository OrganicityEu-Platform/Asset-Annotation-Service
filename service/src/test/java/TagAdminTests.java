//import com.fasterxml.jackson.databind.ObjectMapper;
//import Annotation;
//import Experiment;
//import Service;
//import Tag;
//import TagDomain;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Created by etheodor on 18/05/2016.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
////@SpringApplicationConfiguration(classes = Experiment.class)
////@WebIntegrationTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class TagAdminTests {
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Value("${local.server.port}")
//    int port;
//
//    String tagDomain = "urn:tagDomain:td1";
//    String tagUrn1 = "urn:tag:t1";
//    String tagUrn2 = "urn:tag:t2";
//    String service = "urn:service:s1";
//    String assetUrn = "urn:asset:a1";
//    String application = "urn:application:a1";
//
//    TagDomain td = new TagDomain();
//    Service s = new Service();
//    Experiment a = new Experiment();
//
//
//    @Before
//    public void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        td.setUrn(tagDomain);
//
//    }
//
//    void createTagDomain() throws Exception {
//        mockMvc.perform(post("/admin/tagDomains")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(td)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(tagDomain));
//
//    }
//
//    void updateTagDomain() throws Exception {
//        mockMvc.perform(post("/admin/tagDomains/" + td.getUrn())
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(td)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(tagDomain))
//                .andExpect(jsonPath("$.description").value("description")
//                );
//    }
//
//    void getTagDomain() throws Exception {
//        mockMvc.perform(get("/tagDomains/" + tagDomain))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(tagDomain));
//    }
//
//    void deleteTagDomain() throws Exception {
//        mockMvc.perform(delete("/admin/tagDomains/" + tagDomain))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""));
//    }
//
//    void addTag1() throws Exception {
//        Tag tag = new Tag();
//        tag.setUrn(tagUrn1);
//        mockMvc.perform(post("/admin/tagDomains/" + td.getUrn() + "/tags")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(tag)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(tagUrn1));
//    }
//
//    void addTag2() throws Exception {
//        Tag tag = new Tag();
//        tag.setUrn(tagUrn2);
//        mockMvc.perform(post("/admin/tagDomains/" + td.getUrn() + "/tags")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(tag)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(tagUrn2));
//    }
//
//    void deleteTag2() throws Exception {
//        mockMvc.perform(delete("/admin/tagDomains/" + tagDomain + "/tags")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(tagUrn2)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""));
//    }
//
//    void deleteTag1() throws Exception {
//        mockMvc.perform(delete("/admin/tagDomains/" + tagDomain + "/tags")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(tagUrn1)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""));
//    }
//
//    void getTags() throws Exception {
//        mockMvc.perform(get("/admin/tagDomains/" + tagDomain + "/tags/"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/tags/" + tagUrn1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(tagUrn1));
//    }
//
//    void printTagDomain() throws Exception {
//        MvcResult r = mockMvc.perform(get("/tagDomains/" + tagDomain))
//                .andExpect(status().isOk()).andReturn();
//
//        System.out.println(r.getResponse().getContentAsString());
//    }
//
//
//    void createService() throws Exception {
//        s.setUrn(service);
//        mockMvc.perform(post("/admin/services")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(s)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(service));
//    }
//
//    void printService() throws Exception {
//        MvcResult r = mockMvc.perform(get("/services/" + service))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(service)).andReturn();
//        System.out.println(r.getResponse().getContentAsString());
//    }
//
//    void printTagDomainsOfService() throws Exception {
//        MvcResult r = mockMvc.perform(get("/admin/services/" + service + "/tagDomains"))
//                .andExpect(status().isOk()
//                ).andReturn();
//        System.out.println(r.getResponse().getContentAsString());
//    }
//
//    void deleteService() throws Exception {
//        mockMvc.perform(delete("/admin/services/" + service))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""));
//    }
//
//    void addDomainToService() throws Exception {
//        mockMvc.perform(post("/admin/tagDomains/" + tagDomain + "/services") //admin//{}/
//                .param("serviceUrn", service))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(tagDomain));
//    }
//
//
//    void removeDomainFromService() throws Exception {
//        mockMvc.perform(delete("/admin/tagDomains/" + tagDomain + "/services")
//                .param("serviceUrn", service))
//                .andExpect(status().isOk());
//    }
//
//
//    void createApplication() throws Exception {
//        a.setUrn(application);
//        mockMvc.perform(post("/admin/applications")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(a)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(application));
//    }
//
//    void printApplication() throws Exception {
//        MvcResult r = mockMvc.perform(get("/applications/" + application))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(application)).andReturn();
//        System.out.println(r.getResponse().getContentAsString());
//    }
//
//    void printTagDomainsOfApplication() throws Exception {
//        MvcResult r = mockMvc.perform(get("/admin/applications/" + application + "/tagDomains"))
//                .andExpect(status().isOk()
//                ).andReturn();
//        System.out.println(r.getResponse().getContentAsString());
//    }
//
//    void deleteApplication() throws Exception {
//        mockMvc.perform(delete("/admin/applications/" + application))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""));
//    }
//
//    void addDomainToApplication() throws Exception {
//        mockMvc.perform(post("/admin/applications/" + application + "/tagDomains") //admin//{}/
//                .param("tagDomainUrn", tagDomain))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.urn").value(application));
//    }
//
//
//    void removeDomainFromApplication() throws Exception {
//        mockMvc.perform(delete("/admin/applications/" + application + "/tagDomains")
//                .param("tagDomainUrn", tagDomain))
//                .andExpect(status().isOk());
//    }
//
//    void createAnnotation() throws Exception {
//        Annotation annotation = new Annotation();
//        annotation.setAssetUrn(assetUrn);
//        annotation.setTagUrn(tagUrn1);
//        annotation.setExperiment("none");
//        MvcResult r = mockMvc.perform(post("/annotations/" + assetUrn)
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(annotation)))
//                //.andExpect(status().isOk())
//                //.andExpect(jsonPath("$.assetUrn").value(assetUrn))
//                .andReturn();
//        System.out.println(r.getResponse().getContentAsString());
//    }
//
//    void printAnnotations() throws Exception {
//        MvcResult r = mockMvc.perform(get("/annotations/" + assetUrn))
//                .andExpect(status().isOk()
//                ).andReturn();
//        System.out.println("Annotations:" + r.getResponse().getContentAsString());
//    }
//
//
//    void updateAnnotation() throws Exception {
//        Annotation annotation = new Annotation();
//        annotation.setAssetUrn(assetUrn);
//        annotation.setTagUrn(tagUrn1);
//        annotation.setExperiment(application);
//        MvcResult r = mockMvc.perform(patch("/annotations/" + assetUrn)
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(new ObjectMapper().writeValueAsBytes(annotation)))
//                //.andExpect(status().isOk())
//                //.andExpect(jsonPath("$.assetUrn").value(assetUrn))
//                .andReturn();
//        System.out.println(r.getResponse().getContentAsString());
//    }
//
//    @Test // Create, Update, Get, Delete a TagDomain and its Tags
//    public void test1_tagDomainsTagsRelations() throws Exception {
//        createTagDomain();
//        td.setDescription("description");
//        updateTagDomain();
//        getTagDomain();
//        addTag1();
//        addTag2();
//        deleteTag2();
//        getTags();
//        deleteTag1();
//        deleteTagDomain();
//        createTagDomain();
//        td.setDescription("description");
//        updateTagDomain();
//        getTagDomain();
//        addTag1();
//        addTag2();
//        printTagDomain();
//        mockMvc.perform(delete("/admin/tagDomains/" + tagDomain))
//                .andExpect(status().isMethodNotAllowed())
//                .andExpect(content().string("Error Message:TagDomain is not empty"));
//        createService();
//        printService();
//        printTagDomain();
//        addDomainToService();
//        printTagDomainsOfService();
//        createApplication();
//        printApplication();
//        addDomainToApplication();
//        printTagDomainsOfApplication();
//        createAnnotation();
//        printTagDomain();
//        createAnnotation();
//
//
//        printAnnotations();
//        removeDomainFromApplication();
//        removeDomainFromService();
//        printTagDomain();
//        deleteTag2();
//        deleteTag1();
//        deleteTagDomain();
//        deleteApplication();
//        deleteService();
//    }
//
//    @After
//    public void setDown() {
//
//
//    }
//
//}