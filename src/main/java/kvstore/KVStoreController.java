package kvstore;

import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@RestController
public class KVStoreController {
    @Value("${kvstore.replication.host}")
    private String host;
    @Autowired
    private KVStorageService kvStorageService;
    @Autowired
    private KVStorageReplicationService kvStorageReplicationService;

    @RequestMapping(value = "/set/{key}", method=RequestMethod.POST, consumes="application/json")
    public void setKey(@PathVariable("key") String key,
                       @RequestBody String requestJson) {
        this.kvStorageService.put(key, requestJson);
        System.out.println("=========================="+key+" reqeustJson = "+requestJson);
        this.kvStorageReplicationService.putKeyValue(key, requestJson);
    }

    @RequestMapping(value = "/replicate/{key}", method=RequestMethod.POST, consumes="application/json")
    public void replicateKey(@PathVariable("key") String key, @RequestBody String value) {
        System.out.println("key = "+key+" value "+value);
        this.kvStorageService.put(key, value);
    }

    @RequestMapping("/get/{key}")
    public String getKey(@PathVariable("key") String key) {
        System.out.println("key "+key);
        return kvStorageService.get(key);
    }

    @RequestMapping(value = "/health", method=RequestMethod.GET)
    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<String>("true", HttpStatus.OK);
    }
}
