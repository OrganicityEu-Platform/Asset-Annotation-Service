package eu.organicity.annotation.tests.client;


import eu.organicity.annotation.client.AnnotationServiceClient;
import eu.organicity.annotation.common.dto.TagDTO;
import eu.organicity.annotation.common.dto.TagDomainDTO;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

import static eu.organicity.annotation.tests.client.TestsConstants.LIVE2;
import static eu.organicity.annotation.tests.client.TestsConstants.TOKEN;

public class TagDomainTests {
    
    private static final String TAG_DOMAIN = "urn:oc:tagDomain:t1";
    
    private AnnotationServiceClient client;
    
    @Before
    public void before() {
        BasicConfigurator.configure();
        client = new AnnotationServiceClient(TOKEN, LIVE2);
    }
    
    @Test
    public void testCreateEmpty() throws InterruptedException {
        
        System.out.println("Removing domain:" + TAG_DOMAIN);
        client.removeTagDomain(TAG_DOMAIN);
        
        System.out.println("Adding domain:" + TAG_DOMAIN);
        TagDomainDTO domain = client.addTagDomain(TAG_DOMAIN, "test tag domain 1");
        System.out.println(domain);
        Assert.notNull(domain);
        Assert.notNull(domain.getTags());
        Assert.isTrue(0 == domain.getTags().size());
        
        TagDomainDTO finalDomain = client.getTagDomain(TAG_DOMAIN);
        System.out.println(finalDomain);
        Assert.notNull(finalDomain);
        Assert.notNull(finalDomain.getTags());
        Assert.isTrue(0 == finalDomain.getTags().size());
        
    }
    
    @Test
    public void testCreateAll() throws InterruptedException {
        
        System.out.println("Removing domain:" + TAG_DOMAIN);
        client.removeTagDomain(TAG_DOMAIN);
        
        System.out.println("Adding domain:" + TAG_DOMAIN);
        TagDomainDTO domain = client.addTagDomain(TAG_DOMAIN, "test tag domain 1");
        System.out.println(domain);
        Assert.notNull(domain);
        Assert.notNull(domain.getTags());
        Assert.isTrue(0 == domain.getTags().size());
        
        for (int i = 0; i < 4; i++) {
            String tag = TAG_DOMAIN + ":" + i;
            System.out.println("Adding tag:" + tag);
            TagDTO finalTag = client.addTag(TAG_DOMAIN, tag, "tag " + i);
            System.out.println(finalTag);
            Assert.notNull(finalTag);
        }
        
        TagDomainDTO finalDomain = client.getTagDomain(TAG_DOMAIN);
        System.out.println(finalDomain);
        Assert.notNull(finalDomain);
        Assert.notNull(finalDomain.getTags());
        Assert.isTrue(4 == finalDomain.getTags().size());
        
    }
    
    @Test
    public void testCreateAllAtOnce() throws InterruptedException {
        
        System.out.println("Removing domain:" + TAG_DOMAIN);
        client.removeTagDomain(TAG_DOMAIN);
        
        System.out.println("Adding domain:" + TAG_DOMAIN);
        final Set<TagDTO> tagList = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            String tag = TAG_DOMAIN + ":" + i;
            System.out.println("Adding tag:" + tag);
            TagDTO finalTag = new TagDTO();
            finalTag.setUrn(tag);
            finalTag.setName("tag " + i);
            tagList.add(finalTag);
        }
        System.out.println("tagList:" + tagList);
        TagDomainDTO domain = client.addTagDomain(TAG_DOMAIN, "test tag domain 1", tagList);
        System.out.println(domain);
        System.out.println(domain.getTags());
        Assert.notNull(domain);
        Assert.notNull(domain.getTags());
        Assert.isTrue(tagList.size() == domain.getTags().size());
    }
}
