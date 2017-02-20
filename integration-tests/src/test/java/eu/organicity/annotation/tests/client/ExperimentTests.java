package eu.organicity.annotation.tests.client;


import eu.organicity.annotation.client.AnnotationServiceClient;
import eu.organicity.annotation.common.dto.ExperimentDTO;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

public class ExperimentTests {

    private static final String TAG_DOMAIN = "urn:oc:tagDomain:experiments:58419821945e11c10d1f8d69";

    private AnnotationServiceClient client;

    @Before
    public void before() {
        BasicConfigurator.configure();
        client = new AnnotationServiceClient(
                "eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJlYTRhNzZlYi1mZDM0LTQwZDgtOTY0My01NWYxOTdmM2YxYjkiLCJleHAiOjE0ODc2MjA0NzcsIm5iZiI6MCwiaWF0IjoxNDg3NjIwMTc3LCJpc3MiOiJodHRwczovL2FjY291bnRzLm9yZ2FuaWNpdHkuZXUvcmVhbG1zL29yZ2FuaWNpdHkiLCJhdWQiOiJhbm5vdGF0aW9ucy1kZXYiLCJzdWIiOiI0NzdhMDQzMC1hMTI0LTQwOTQtYTA0Ni1jY2I0MTFjMDRjYmYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhbm5vdGF0aW9ucy1kZXYiLCJzZXNzaW9uX3N0YXRlIjoiNTBhMDViNTEtMjUwNi00NmUyLWEyN2MtMWRiZDhlNjc4MTRmIiwiY2xpZW50X3Nlc3Npb24iOiIzNzM1NzcyNi0zMmIwLTQ3MGYtOGQ5ZC05ZTc1NzNhMzgzN2UiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDg0LyJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiYWRtaW5pc3RyYXRvciIsImV4cGVyaW1lbnRlciIsInBhcnRpY2lwYW50Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiZGVtby1vY3NpdGUiOnsicm9sZXMiOlsiZGVsZXRlLWFzc2V0IiwicmVhZC1hc3NldCIsImNyZWF0ZS1hc3NldCIsInVwZGF0ZS1hc3NldCJdfSwic2l0ZS1tYW5hZ2VyIjp7InJvbGVzIjpbInVzZXItdmlld2VyIiwiZGljdGlvbmFyeS1hZG1pbiIsImV4cGVyaW1lbnQtdXNlciIsImRldmVsb3BlciIsImRpY3Rpb25hcnktdXNlciIsIm1ldHJpY3MiLCJyb2xlLWFkbWluIl19LCJzaXRlLW1hbmFnZXItZGV2Ijp7InJvbGVzIjpbImRpY3Rpb25hcnktYWRtaW4iLCJ1c2VyLXZpZXdlciIsImV4cGVyaW1lbnQtdXNlciIsImRldmVsb3BlciIsImRpY3Rpb25hcnktdXNlciIsIm1ldHJpY3MiLCJyb2xlLWFkbWluIl19LCJzY2VuYXJpb3MiOnsicm9sZXMiOlsibW9kZXJhdG9yIiwiYWRtaW4iXX19LCJuYW1lIjoiRGltaXRyaW9zIEFtYXhpbGF0aXMiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbWF4aWxhdEBjdGkuZ3IiLCJnaXZlbl9uYW1lIjoiRGltaXRyaW9zIiwiZmFtaWx5X25hbWUiOiJBbWF4aWxhdGlzIiwiZW1haWwiOiJhbWF4aWxhdEBjdGkuZ3IifQ.GGG0BG7P33e9Ioqu53c4TUkmHo_bMb5buBAW7tCcONZ-MKn1-q3oCi66eegxRdZyuwFJXiVm3hciePbhfKt6YammryLaxIIXJaj70SmwDRpSwbH1C9ftHZNR-4YXK22crTRxBEmm_fCW6fZZ0hheuyZjHV-Bx9ErbQTbwrdhDeaThNjih_rx0IcOrFP3IRUZjjgQ-UB1LaMv9OzSDNH7YfVZW-8kol_chc3gBZXrvUQ8fFDgsHweQD84CIOOGt9Ha2DEywjXsH6XBb3YjBfhhUiNRZfyhsWNiQnpInZJ407ezEgI7_C99ccQoYYOpu3mQsEmznvettdS3A12ZJP8TQ"
                , "http://localhost:8084"
        );
    }

    @Test
    public void testCreateEmpty() throws InterruptedException {
        System.out.println("Adding experiment:" + TAG_DOMAIN);
        ExperimentDTO experiment = client.experimentCreate(TAG_DOMAIN);
        System.out.println(experiment);
        Assert.notNull(experiment);
        Assert.notNull(experiment.getTagDomains());
        Assert.notNull(experiment.getUrn());
        Assert.isTrue(1 == experiment.getTagDomains().size());

    }

}
