package kvstore;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

@Service
public class KVStorageServiceImpl implements KVStorageService {

    private static final Integer BUCKET_SIZE = 20;
    private static final List<ConcurrentHashMap<String, String>> arrayMapList = new ArrayList<ConcurrentHashMap<String, String>>();

    static {
        for(int i=0; i < BUCKET_SIZE; i++) {
            arrayMapList.add(new ConcurrentHashMap<String, String>());
        }
    }

    @Override
    public void put(String key, String value) {
        int bucket = Math.abs(key.hashCode()%BUCKET_SIZE-1);
        System.out.println("bucket = "+bucket);
        arrayMapList.get(bucket).put(key, value);
    }

    @Override
    public String get(String key) {
        int bucket = Math.abs(key.hashCode()%BUCKET_SIZE-1);
        System.out.println("bucket = "+bucket);
        return  arrayMapList.get(bucket).get(key);
    }
}
