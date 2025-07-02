package self.boltclone.containerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class ContainerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContainerServerApplication.class, args);
    }

}
