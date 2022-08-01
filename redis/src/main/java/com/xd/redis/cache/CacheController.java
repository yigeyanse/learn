package com.xd.redis.cache;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cache.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@CacheConfig(cacheNames = "myCache")
public class CacheController {

    @GetMapping("cache1")
    @Cacheable(cacheNames = "users",key = "#i",condition = "#i > 1")
    public Object cache1(int i){
        System.out.println("5555555555555555");
        JSONObject source = new JSONObject();
        source.put("icon_url","https://wework.qpic.cn/wwpic/252813_jOfDHtcISzuodLa_1629280209/0");
        source.put("desc","企业微信");
        source.put("desc_color",0);
        return source;
    }

    /**
     * 应用到写数据的方法上，如新增/修改方法，调用方法时会自动把相应的数据放入缓存
     *
     * 在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中
     * @param i
     * @return
     */
    @GetMapping("cache2")
    @CachePut(value = "user", key = "#i")
    public Object cache2(int i){
        System.out.println("44444444444444");
        JSONObject source = new JSONObject();
        source.put("icon_url","https://wework.qpic.cn/wwpic/252813_jOfDHtcISzuodLa_1629280209/0");
        source.put("desc","企业微信");
        source.put("desc_color",i);
        return source;
    }

    /**
     *
     * @param i
     */
    @GetMapping("cache3")
    @CacheEvict(value = "user", key = "#i",allEntries = true)
    public void cache3(int i){

    }

/*    @Caching(
            put = {
                    @CachePut(value = "user", key = "#user.id"),
                    @CachePut(value = "user", key = "#user.username"),
                    @CachePut(value = "user", key = "#user.age")
                }
    )*/
    @Caching(
            cacheable = {
                    @Cacheable(value = "emp",key = "#lastName")
            },
            put = {
                    @CachePut(value = "emp",key = "#result.id"),
                    @CachePut(value = "emp",key = "#result.email")
            },
            evict = {
                    @CacheEvict(cacheNames = "emp",allEntries = true)
            }
    )
    @GetMapping("cache4")
    public void cache4(int i){

    }

    @GetMapping("cache5")
    @Cacheable({"cache1", "cache2"})
    public Object cache5(int i){
        System.out.println("666666666666");
        JSONObject source = new JSONObject();
        source.put("icon_url","https://wework.qpic.cn/wwpic/252813_jOfDHtcISzuodLa_1629280209/0");
        source.put("desc","企业微信");
        source.put("desc_color",i);
        return source;
    }
}
