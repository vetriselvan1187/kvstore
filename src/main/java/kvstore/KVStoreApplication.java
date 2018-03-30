package kvstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KVStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(KVStoreApplication.class, args);
    }
}
