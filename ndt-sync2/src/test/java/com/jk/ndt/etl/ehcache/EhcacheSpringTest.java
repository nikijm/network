package com.jk.ndt.etl.ehcache;

import com.jk.ndt.etl.SpringTest;
import com.jk.ndt.etl.entity.system.User;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

/**
 * Created by 朱生 on 2017/5/23.
 */
public class EhcacheSpringTest extends SpringTest{

    @Autowired
    private Cache cache;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void init() {
//        ApplicationContext ctx = new ClassPathXmlApplicationContext(
//                "applicationContext.xml");
//        EhCacheCacheManager ehCacheManager = (EhCacheCacheManager) ctx
//                .getBean("cacheManager");
//        cacheManager = ehCacheManager.getCacheManager();
//        cache = cacheManager.getCache("userCache");
    }

    @After
    public void destory() {
        cacheManager.shutdown();
    }

    @Test
    public void testEhcacheString() {
        String key = "hello";
        String value = "world";
        cache.put(new Element(key, value));
        Element element = cache.get(key);
        Object obj = element.getObjectValue();
        Assert.assertEquals(value, obj);
        System.out.println(obj);
    }

    @Test
    public void testEhcacheObj() {
        String key = "user";
        User value = new User();
        value.setId(new BigDecimal(1));
        value.setName("test");
        cache.put(new Element(key, value));
        Element element = cache.get(key);
        Object obj = element.getObjectValue();
        Assert.assertEquals(value, obj);
        System.out.println(obj);
        Object objSer = element.getValue();
        Assert.assertEquals(value, objSer);
        System.out.println(objSer);
    }
}
