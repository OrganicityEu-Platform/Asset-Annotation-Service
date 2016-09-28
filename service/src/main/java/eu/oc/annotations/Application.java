package eu.oc.annotations;

import com.google.common.base.Predicate;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@ComponentScan({"eu.oc.annotations.config", "eu.oc.annotations.controller", "eu.oc.annotations.dto.converter", "eu.oc.annotations.handlers", "eu.oc.annotations.service"})
@EnableNeo4jRepositories(basePackages = "eu.oc.annotations.repositories")
@EnableTransactionManagement
@EnableSwagger2
@EnableAutoConfiguration
public class Application extends Neo4jConfiguration {

    @Value("${neo4j.httpPath}")
    String databasePath;

    public Application() {

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory(getConfiguration(), "eu.oc.annotations.domain");
    }

  /*  @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.DEFAULT)
    public Session getSession() throws Exception {
        return super.getSession();
    }*/

    @Bean
    @Autowired
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config.driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
                //.setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver") //
                .setURI(databasePath);
        return config;
    }

    @Bean
    public Docket annotationApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Annotation-API")
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .build();
    }

    private Predicate<String> paths() {
        return or(
                regex("/tagDomain.*"),
                regex("/application.*"),
                regex("/annotations.*"),
                regex("/tags.*"),
                regex("/admin.*")
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Organicity Tagging API")
                .description("Organicity Tagging API")
                .termsOfServiceUrl("http://www.organicity.eu")
                .version("0.1").build();
    }


    @Bean
    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return servletContainer -> ((TomcatEmbeddedServletContainerFactory) servletContainer).addConnectorCustomizers(
                (TomcatConnectorCustomizer) connector -> {
                    AbstractHttp11Protocol httpProtocol = (AbstractHttp11Protocol) connector.getProtocolHandler();
                    httpProtocol.setCompression("on");
                    httpProtocol.setCompressionMinSize(256);
                    String mimeTypes = httpProtocol.getCompressableMimeTypes();
                    String mimeTypesWithJson = mimeTypes
                            + "," + MediaType.APPLICATION_JSON_VALUE
                            + "," + MediaType.IMAGE_PNG + "," + MediaType.IMAGE_GIF + "," + MediaType.IMAGE_JPEG
                            + "," + "application/javascript" + "," + "text/css";
                    httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
                }
        );
    }


}
