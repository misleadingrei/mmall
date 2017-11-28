package com.mmall.common;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by misleadingrei on 11/27/17.
 */
public class TokenCache  {


     private  static Logger logger = LoggerFactory.getLogger(TokenCache.class);


    // apply for  memory block
    //set cache capacity to 1000
    // use lru method
    //use caller chains
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(10000).maximumSize(1000).expireAfterAccess(12, TimeUnit.HOURS).
            build(new CacheLoader<String, String>() {
                @Override
                //default load impl when call get ,if key does not have a corresponding value,this method whill be load
                public String load(String s) throws Exception {
                    return "null" ;
                }
            });

    //setter

    public static  void setKey(String key ,String value){
        localCache.put(key,value);
    }


    //getter
    public static  String  getKey(String key){
               String value=null;
       try {
             value=localCache.get(key);
           if("null".equals(value))
           {
               return null;
           }
           return value;
       }

       catch (Exception e){
            logger.error("localCache get error",e);
       }
       return null;
    }

}
