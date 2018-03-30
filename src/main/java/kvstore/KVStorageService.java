package kvstore;

public interface KVStorageService {
    public void put(String key, String value);
    public String get(String key);
}
