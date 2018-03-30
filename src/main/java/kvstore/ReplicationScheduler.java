package kvstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;

@Component
public class ReplicationScheduler {

    @Autowired
    private KVStorageReplicationService kvStorageReplicationService;
    @Autowired
    QueueConfig queueConfig;

    @Scheduled(fixedRate = 5000)
    public void replicateKVWrapper() {
        if(!queueConfig.blockingQueue().isEmpty() && kvStorageReplicationService.getLiveStatus()) {
            KVWrapper kvWrapper = queueConfig.blockingQueue().peek();
            HttpStatus httpStatus = kvStorageReplicationService.replicateKeyValue(kvWrapper.getKey(), kvWrapper.getValue());
            if(httpStatus == HttpStatus.OK)
                queueConfig.blockingQueue().remove();
        }
    }
}
