package tog.is.cache.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public abstract class AbstractCallCache<K, V, T>{  
    
    
    /** 
     */  
    public Cache<K, V> cache = CacheBuilder.newBuilder()  
            .maximumSize(CacheConfig.maximumSize)  
            .expireAfterWrite(CacheConfig.expireAfterWrite, TimeUnit.MINUTES)  
            .build();  
      
    /** 
     * @param key 
     * @param t 
     * @return 
     */  
    public V getCacheData(final K key, final T t){  
        try {  
            return cache.get(key, new Callable<V>() {  
  
                public V call() throws Exception {  
                    return getData(key, t);  
                }  
            });  
        } catch (ExecutionException e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
      
    /** 
     * @return v 
     */  
    public abstract V getData(K key, T t);  
  
      
      
    public Cache<K, V> getCache() {  
        return cache;  
    }  
  
    public void setCache(Cache<K, V> cache) {  
        this.cache = cache;  
    }  
      
}  