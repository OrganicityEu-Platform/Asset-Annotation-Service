package eu.organicity.annotation;

import com.google.common.base.Predicate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"eu.organicity.annotation.config", "eu.organicity.annotation.controller", "eu.organicity.annotation.dto.converter", "eu.organicity.annotation.handlers", "eu.organicity.annotation.service"})
@EnableJpaRepositories(basePackages = "eu.organicity.annotation.repositories")
@EnableCaching
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
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

//    @Bean
//    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
//        return servletContainer -> ((TomcatEmbeddedServletContainerFactory) servletContainer).addConnectorCustomizers(
//                (TomcatConnectorCustomizer) connector -> {
//                    AbstractHttp11Protocol httpProtocol = (AbstractHttp11Protocol) connector.getProtocolHandler();
//                    httpProtocol.setCompression("on");
//                    httpProtocol.setCompressionMinSize(256);
//                    String mimeTypes = httpProtocol.getCompressableMimeTypes();
//                    String mimeTypesWithJson = mimeTypes
//                            + "," + MediaType.APPLICATION_JSON_VALUE
//                            + "," + MediaType.IMAGE_PNG + "," + MediaType.IMAGE_GIF + "," + MediaType.IMAGE_JPEG
//                            + "," + "application/javascript" + "," + "text/css";
//                    httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
//                }
//        );
//    }

}
