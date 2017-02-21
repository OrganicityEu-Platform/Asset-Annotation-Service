package eu.organicity.annotation.tests.client;


import eu.organicity.annotation.client.AnnotationServiceClient;
import eu.organicity.annotation.common.dto.ServiceDTO;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import static eu.organicity.annotation.tests.client.TestsConstants.LIVE2;
import static eu.organicity.annotation.tests.client.TestsConstants.TOKEN;

public class ServiceTests {
    
    private static final String TAG_DOMAIN = "urn:oc:tagDomain:experiments:58419821945e11c10d1f8d69";
    
    private AnnotationServiceClient client;
    
    @Before
    public void before() {
        BasicConfigurator.configure();
        client = new AnnotationServiceClient(TOKEN, LIVE2);
    }
    
    @Test
    public void testCreateEmpty() throws InterruptedException {
        System.out.println("Adding service:" + TAG_DOMAIN);
        ServiceDTO experiment = client.servicesCreate(TAG_DOMAIN);
        System.out.println(experiment);
        Assert.notNull(experiment);
        Assert.notNull(experiment.getUrn());
    }
    
}
