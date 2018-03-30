package kvstore;

import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.context.annotation.Bean;

@Configuration
public class QueueConfig {

    @Bean
    public BlockingQueue<KVWrapper> blockingQueue() {
        return new LinkedBlockingDeque<KVWrapper>();
    }
}
