package kvstore;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KVStoreTest {

    @BeforeClass
    public static void setup() {
        SpringApplicationBuilder kvStore1 = new SpringApplicationBuilder(KVStoreApplication.class)
                .properties("server.port:4455","kvstore.replication.host:http://localhost:4466");
        SpringApplicationBuilder kvStore2 = new SpringApplicationBuilder(KVStoreApplication.class)
                .properties("server.port:4466","kvstore.replication.host:http://localhost:4455");
        kvStore2.run();
        kvStore1.run();
    }

    @Test
    public void test1PostKeyValue() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String value = "{\"name\":\"Vetriselvan\",\"email\":\"vetrblogger1187@gmail.com\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(value, headers);

        // send request and parse result
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("/set/vetri", 4466), HttpMethod.POST, entity, String.class);
        System.out.println(response);
        assert(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void test2RetreiveKeyValue() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String value = "{\"name\":\"Vetriselvan\",\"email\":\"vetrblogger1187@gmail.com\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        // send request and parse result
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("/get/vetri", 4466), HttpMethod.GET, entity, String.class);
        assert(response.getBody().equals(value));
    }

    private String createURLWithPort(String uri, int port) {
        return "http://localhost:"+port+uri;
    }
}
