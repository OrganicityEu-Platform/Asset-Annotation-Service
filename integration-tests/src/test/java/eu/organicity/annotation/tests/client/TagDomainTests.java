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

public class TagDomainTests {

    private static final String TAG_DOMAIN = "urn:oc:tagDomain:t1";

    private AnnotationServiceClient client;

    @Before
    public void before() {
        BasicConfigurator.configure();
        client = new AnnotationServiceClient(
                "eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJlODY1MjUxNi05ZWU4LTQwNWEtOGIxYS04NDM1ZjAzZDM2Y2IiLCJleHAiOjE0ODc2MTk3NTcsIm5iZiI6MCwiaWF0IjoxNDg3NjE5NDU3LCJpc3MiOiJodHRwczovL2FjY291bnRzLm9yZ2FuaWNpdHkuZXUvcmVhbG1zL29yZ2FuaWNpdHkiLCJhdWQiOiJhbm5vdGF0aW9ucy1kZXYiLCJzdWIiOiI0NzdhMDQzMC1hMTI0LTQwOTQtYTA0Ni1jY2I0MTFjMDRjYmYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhbm5vdGF0aW9ucy1kZXYiLCJzZXNzaW9uX3N0YXRlIjoiNTBhMDViNTEtMjUwNi00NmUyLWEyN2MtMWRiZDhlNjc4MTRmIiwiY2xpZW50X3Nlc3Npb24iOiIwODdiMjVlOS1hMzI0LTRhNjctYTRkNC1jYTUwOGMxNjQ1NWIiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDg0LyJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiYWRtaW5pc3RyYXRvciIsImV4cGVyaW1lbnRlciIsInBhcnRpY2lwYW50Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiZGVtby1vY3NpdGUiOnsicm9sZXMiOlsiZGVsZXRlLWFzc2V0IiwicmVhZC1hc3NldCIsImNyZWF0ZS1hc3NldCIsInVwZGF0ZS1hc3NldCJdfSwic2l0ZS1tYW5hZ2VyIjp7InJvbGVzIjpbInVzZXItdmlld2VyIiwiZGljdGlvbmFyeS1hZG1pbiIsImV4cGVyaW1lbnQtdXNlciIsImRldmVsb3BlciIsImRpY3Rpb25hcnktdXNlciIsIm1ldHJpY3MiLCJyb2xlLWFkbWluIl19LCJzaXRlLW1hbmFnZXItZGV2Ijp7InJvbGVzIjpbImRpY3Rpb25hcnktYWRtaW4iLCJ1c2VyLXZpZXdlciIsImV4cGVyaW1lbnQtdXNlciIsImRldmVsb3BlciIsImRpY3Rpb25hcnktdXNlciIsIm1ldHJpY3MiLCJyb2xlLWFkbWluIl19LCJzY2VuYXJpb3MiOnsicm9sZXMiOlsibW9kZXJhdG9yIiwiYWRtaW4iXX19LCJuYW1lIjoiRGltaXRyaW9zIEFtYXhpbGF0aXMiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbWF4aWxhdEBjdGkuZ3IiLCJnaXZlbl9uYW1lIjoiRGltaXRyaW9zIiwiZmFtaWx5X25hbWUiOiJBbWF4aWxhdGlzIiwiZW1haWwiOiJhbWF4aWxhdEBjdGkuZ3IifQ.Jeax9oWpfSH860B0DV4fwikwU46SQo9UD8xE38LINxCWYolcxCbuPmugjwPMqJa-BIJoUjNlTKmCjXWOW0kG9EZm-f7fHUT3oHQjcP9_RCbHePPwzseGBNpK2Rbcx-rnrZF0Irb0knpxn00HacV2KTAU4LSpfrDaFUSqrVXLcfozrIE9Rjqw9LDpGOXkYYuc4bCpEi0UewGimJBzRtlCZ5644DnBIAt8m9orkxC2dUIlekTAb4YwU8DLRNEGpMtAJsQW2ku9so3sMwZJc8Vtehra26_KJCzH2A5KIwYki94jSacbg2xorvPs0W2OyEeqXccI8KKPSqjncCGq3zYNVg"
                , "http://localhost:8084"
        );
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
