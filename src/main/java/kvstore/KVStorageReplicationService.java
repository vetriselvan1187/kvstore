package kvstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class KVStorageReplicationService {

    @Value("${kvstore.replication.host}")
    private String host;
    @Autowired
    QueueConfig queueConfig;
    private RestTemplate restTemplate = new RestTemplate();

    public void putKeyValue(String key, String value) {
        boolean replicationSuccess = false;
        try {
            HttpStatus httpStatus = replicateKeyValue(key, value);
            replicationSuccess = (httpStatus == HttpStatus.OK) ? true : false;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        if(!replicationSuccess) {
            try {
                queueConfig.blockingQueue().put(new KVWrapper(key, value));
            }catch (InterruptedException ex) {

            }
        }
    }

    public HttpStatus replicateKeyValue(String key, String value) {
        String url = host + "/replicate/" + key;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(value, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getStatusCode();
    }

    public String getValue(String key) {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("/get/"+key), HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    private String createURLWithPort(String uri) {
        return host + uri;
    }

    public boolean getLiveStatus() {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("/health"), HttpMethod.GET, entity, String.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
}

final class KVWrapper {
    private final String key;
    private final String value;

    public KVWrapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}