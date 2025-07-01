package self.boltclone.containerserver.repository.impl.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.ecs.EcsClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Bean
    public EcsClient ecsClient() {

        return EcsClient.builder().endpointOverride(URI.create("http://localhost:4566")).build();
    }

}
