package xinrui.cloud.config;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * <B>Title:</B>EhcacheConfig</br>
 * <B>Description:</B>EHCACHE缓存处理</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/19 16:45
 */
@Lazy
@Component
@Primary
public final class EhcacheComponent {

    private final CacheManager ehCacheManager;

    private final static String USER_LOGOUT_TOKEN_CACHE="user_logout_token_cache";

    /**
     * 初始化创建一个{@link CacheManager}缓存管理对象
     */
    public EhcacheComponent(){
        ehCacheManager=CacheManager.create();
        addCache(setCacheConfigWithName(USER_LOGOUT_TOKEN_CACHE));
    }

    private void addCache(CacheConfiguration cacheConfiguration) {
        ehCacheManager.addCache(new Cache(cacheConfiguration));
    }

    private CacheConfiguration setCacheConfigWithName(String cacheName) {
        CacheConfiguration cacheConfiguration=new CacheConfiguration();
        cacheConfiguration.setName(cacheName);
        cacheConfiguration.setEternal(true);
        cacheConfiguration.setTimeToIdleSeconds(0);
        cacheConfiguration.setTimeToLiveSeconds(JwtAccessTokenConverterEnhance.EXPIRATION_TIME);
        cacheConfiguration.setOverflowToDisk(false);
        cacheConfiguration.setStatistics(true);
        cacheConfiguration.setMaxEntriesLocalHeap(1024*1024);
        return cacheConfiguration;
    }

    public Cache getCacheByName(String name){
        Assert.hasText(name,"cache name must not be null!");
        return ehCacheManager.getCache(name);
    }

    /**
     * 获取默认token缓存
     * @return
     */
    public Cache getDefaultTokenCache(){
        return ehCacheManager.getCache(USER_LOGOUT_TOKEN_CACHE);
    }

    /**
     * 根据{@link Element}对象获取其值，如果为空返回null
     * @param element
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getTValue(Element element) {
        Object objectValue = element.getObjectValue();
        return (T) (objectValue==null? null:objectValue);
    }


}
