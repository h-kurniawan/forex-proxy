package paidy.forex.rates;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class ForexCache {
    private  final Cache cache;

    public ForexCache(
            final CacheManager cacheManager,
            @Value("${cache.name}") final String cacheName) {
        this.cache = cacheManager.getCache(cacheName);
    }

    public <T> T getCache(String key) {
        var cachedObject = this.cache.get(key);
        if (cachedObject != null)
            return (T)cachedObject.get();
        else
            return null;
    }

    public void setCache(String key, Object value) {
        this.cache.put(key, value);
    }
}
