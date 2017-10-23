package eu.organicity.annotation.tests.client;


import eu.organicity.annotation.client.AnnotationServiceClient;
import eu.organicity.annotation.common.dto.AnnotationDTO;
import eu.organicity.annotation.common.dto.CreateAnnotationDTO;
import eu.organicity.annotation.common.dto.TagDTO;
import eu.organicity.annotation.common.dto.TagDomainDTO;

import java.util.HashSet;
import java.util.Set;

public class AnnotationsApiExample {
    public static final String TOKEN = "";
    
    private static final String TAG_DOMAIN = "urn:oc:tagDomain:exampleTD3";
    
    public static void main(String[] args) {
        final AnnotationServiceClient client = new AnnotationServiceClient(TOKEN,"http://localhost:8084/");
        
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
        
        final TagDomainDTO domain = client.addTagDomain(TAG_DOMAIN, "test tag domain 1", tagList);
        System.out.println(domain);
        System.out.println(domain.getTags());
        
        
        final CreateAnnotationDTO dto = new CreateAnnotationDTO();
        dto.setAssetUrn("urn:my:asset");
        dto.setTagUrn(tagList.iterator().next().getUrn());
        dto.setTextValue("my tag message");
        dto.setUser("blah");
        dto.setNumericValue(0.1);
        dto.setApplication("myAwesomeApp");
        final AnnotationDTO annotation = client.postAnnotation(dto);
        System.out.println(annotation);
    }
}
