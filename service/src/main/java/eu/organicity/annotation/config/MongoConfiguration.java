package eu.organicity.annotation.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "net.sparkworks.adw")
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String mongoDbHost;
    @Value("${spring.data.mongodb.port}")
    private String mongoDbPort;
    @Value("${spring.data.mongodb.database}")
    private String mongoDbName;

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(mongoDbHost + ":" + mongoDbPort);
    }

    @Override
    protected String getDatabaseName() {
        return mongoDbName;
    }

    @Override
    protected UserCredentials getUserCredentials() {
        return UserCredentials.NO_CREDENTIALS;
    }
}
