package paidy.forex.rates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ForexCacheTest {
    private  ForexCache forexCache;

    @BeforeEach
    public void setUp() {
        var cacheName = "test-cache";
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache(cacheName)));
        cacheManager.initializeCaches();
        forexCache = new ForexCache(cacheManager, cacheName);
    }

    @Test
    public void shouldExistInCache() {
        var name = "john wick";
        forexCache.set("key", name);
        var cachedName = forexCache.<String>get("key");
        assertThat(cachedName).isEqualTo(name);
    }

    @Test
    public void shouldNotExistInCache() {
        var name = "john wick";
        forexCache.set("key", name);
        var cachedName = forexCache.<String>get("other-key");
        assertThat(cachedName).isNull();
    }}
