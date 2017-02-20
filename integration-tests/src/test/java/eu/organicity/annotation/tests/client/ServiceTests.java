package eu.organicity.annotation.tests.client;


import eu.organicity.annotation.client.AnnotationServiceClient;
import eu.organicity.annotation.common.dto.ServiceDTO;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

public class ServiceTests {

    private static final String TAG_DOMAIN = "urn:oc:tagDomain:experiments:58419821945e11c10d1f8d69";

    private AnnotationServiceClient client;

    @Before
    public void before() {
        BasicConfigurator.configure();
        client = new AnnotationServiceClient(
                "eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI0ZDM0ZTAwNS01NTczLTQ3ODgtOTYwMS1jNWJmNzk2ODRhMWQiLCJleHAiOjE0ODc2MjAxNDUsIm5iZiI6MCwiaWF0IjoxNDg3NjE5ODQ1LCJpc3MiOiJodHRwczovL2FjY291bnRzLm9yZ2FuaWNpdHkuZXUvcmVhbG1zL29yZ2FuaWNpdHkiLCJhdWQiOiJhbm5vdGF0aW9ucy1kZXYiLCJzdWIiOiI0NzdhMDQzMC1hMTI0LTQwOTQtYTA0Ni1jY2I0MTFjMDRjYmYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhbm5vdGF0aW9ucy1kZXYiLCJzZXNzaW9uX3N0YXRlIjoiNTBhMDViNTEtMjUwNi00NmUyLWEyN2MtMWRiZDhlNjc4MTRmIiwiY2xpZW50X3Nlc3Npb24iOiIwNjI4OGRhMy03Y2RkLTQzNjctODA3MS1iZGY5NjRjZjdmYWUiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDg0LyJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiYWRtaW5pc3RyYXRvciIsImV4cGVyaW1lbnRlciIsInBhcnRpY2lwYW50Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiZGVtby1vY3NpdGUiOnsicm9sZXMiOlsiZGVsZXRlLWFzc2V0IiwicmVhZC1hc3NldCIsImNyZWF0ZS1hc3NldCIsInVwZGF0ZS1hc3NldCJdfSwic2l0ZS1tYW5hZ2VyIjp7InJvbGVzIjpbInVzZXItdmlld2VyIiwiZGljdGlvbmFyeS1hZG1pbiIsImV4cGVyaW1lbnQtdXNlciIsImRldmVsb3BlciIsImRpY3Rpb25hcnktdXNlciIsIm1ldHJpY3MiLCJyb2xlLWFkbWluIl19LCJzaXRlLW1hbmFnZXItZGV2Ijp7InJvbGVzIjpbImRpY3Rpb25hcnktYWRtaW4iLCJ1c2VyLXZpZXdlciIsImV4cGVyaW1lbnQtdXNlciIsImRldmVsb3BlciIsImRpY3Rpb25hcnktdXNlciIsIm1ldHJpY3MiLCJyb2xlLWFkbWluIl19LCJzY2VuYXJpb3MiOnsicm9sZXMiOlsibW9kZXJhdG9yIiwiYWRtaW4iXX19LCJuYW1lIjoiRGltaXRyaW9zIEFtYXhpbGF0aXMiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbWF4aWxhdEBjdGkuZ3IiLCJnaXZlbl9uYW1lIjoiRGltaXRyaW9zIiwiZmFtaWx5X25hbWUiOiJBbWF4aWxhdGlzIiwiZW1haWwiOiJhbWF4aWxhdEBjdGkuZ3IifQ.TjirMFVww6r6nKumHEhZG0KdWwoZMSsPYHy1AKBh3Z1tjywReTwqY-rrSECqUQWFSKgqa3_P0zlTKVdHK7Rsw4PDfjXmEzJAR3m0qlDQ60zASCLbD13Slv9q3JSIvFqG9iIHLxnWH6-8G4Z7itWJSwhgZUIkAgD0aBrulF5nEkHIHmp_FnjPb8gHvcpfuM3Q9OvgRkPlkJJO9VhS-kgGnGF0gUvKi68CglGWsaDjfN9XgWJX-uBYCtRC4R3yyNyBJI-lZ230hptu6SXz80CcIWCSp4yZM_kiAVuDzmgqI-AT_Rx3i_TF_b39QL3tH6MNgpD1e148B1Qsjn5LHrm0tA"
                , "http://localhost:8084"
        );
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
