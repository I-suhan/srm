package com.ne.srm.core.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ne.srm.core.dto.RedisLockToken;
import com.ne.srm.core.properties.RedisProperties;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {


    private final RedisTemplate<String,Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisProperties redisProperties;

    private final ObjectMapper objectMapper;



    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT =
            new DefaultRedisScript<>(
                    """
                    if redis.call('get',KEYS[1]) == ARGV[1]
                    then
                        return redis.call('del',KEYS[1])
                    else
                        return 0
                    end
                    """,
                    Long.class
            );



    public RedisService(
            RedisTemplate<String,Object> redisTemplate,
            StringRedisTemplate stringRedisTemplate,
            RedisProperties redisProperties,
            ObjectMapper objectMapper){

        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisProperties = redisProperties;
        this.objectMapper = objectMapper;
    }


    private String prefix(){

        String prefix = redisProperties.getPrefix();

        if(prefix == null || prefix.isBlank()){
            return "";
        }

        return prefix.endsWith(":") ? prefix : prefix + ":";
    }




    private String key(String key){

        Objects.requireNonNull(key, "redis key cannot be null");

        String prefix = prefix();

        return prefix.isEmpty() ? key : prefix + key;
    }


    /*
    =====================
        String
    =====================
     */


    public void set(String key, String value){

        stringRedisTemplate.opsForValue()
                .set(this.key(key), value);
    }


    public void set(
            String key,
            String value,
            Duration timeout){

        stringRedisTemplate.opsForValue()
                .set(
                        this.key(key),
                        value,
                        timeout
                );
    }


    public String get(String key){

        return stringRedisTemplate.opsForValue()
                .get(this.key(key));
    }


    /*
    =====================
        Object
    =====================
     */


    public void setObject(String key, Object value){

        Objects.requireNonNull(
                value,
                "redis value cannot be null"
        );


        redisTemplate.opsForValue()
                .set(this.key(key), value);
    }


    public void setObject(
            String key,
            Object value,
            Duration timeout){

        Objects.requireNonNull(
                value,
                "redis value cannot be null"
        );


        redisTemplate.opsForValue()
                .set(
                        this.key(key),
                        value,
                        timeout
                );
    }


    public boolean setObjectIfAbsent(
            String key,
            Object value,
            Duration timeout){

        Boolean result = redisTemplate
                .opsForValue()
                .setIfAbsent(
                        this.key(key),
                        value,
                        timeout
                );

        return Boolean.TRUE.equals(result);
    }


    public <T> T getObject(String key, Class<T> clazz){

        Object value = redisTemplate.opsForValue()
                .get(this.key(key));

        if(value == null){
            return null;
        }

        if(clazz.isInstance(value)){
            return clazz.cast(value);
        }

        return objectMapper.convertValue(value, clazz);
    }


    public <T> T getObject(String key, TypeReference<T> type){

        Object value = redisTemplate.opsForValue()
                        .get(this.key(key));

        if(value == null){
            return null;
        }

        return objectMapper.convertValue(value, type);
    }


    public <T> Map<String,T> multiGet(Collection<String> keys, Class<T> clazz){

        List<String> originKeys = new ArrayList<>(keys);

        List<String> redisKeys = originKeys.stream().map(this::key).toList();

        List<Object> values = redisTemplate.opsForValue().multiGet(redisKeys);

        Map<String,T> result = new HashMap<>();

        if(values == null){
            return result;
        }

        for(int i = 0; i < values.size(); i++){


            Object value = values.get(i);

            if(value != null){
                T data;

                if(clazz.isInstance(value)){
                    data = clazz.cast(value);
                }else{
                    data = objectMapper.convertValue(value, clazz);
                }

                result.put(originKeys.get(i), data
                );
            }
        }

        return result;
    }



    /*
    =====================
        Hash
    =====================
     */


    public void hashPut(
            String key,
            String hashKey,
            Object value){

        Objects.requireNonNull(
                value,
                "redis hash value cannot be null"
        );


        redisTemplate.opsForHash()
                .put(
                        this.key(key),
                        hashKey,
                        value
                );
    }





    public void hashPutAll(String key, Map<String,Object> data){

        Objects.requireNonNull(
                data,
                "redis hash data cannot be null"
        );


        redisTemplate.opsForHash()
                .putAll(this.key(key), data);
    }






    public <T> T hashGet(
            String key,
            String hashKey,
            Class<T> clazz){


        Object value = redisTemplate.opsForHash()
                .get(this.key(key), hashKey);


        if(value == null){
            return null;
        }


        if(clazz.isInstance(value)){
            return clazz.cast(value);
        }


        return objectMapper.convertValue(value, clazz);
    }


    /*
    =====================
        Counter
    =====================
     */


    public Long increment(
            String key){

        return stringRedisTemplate.opsForValue()
                .increment(this.key(key));
    }

    public Long increment(String key, long delta){

        return stringRedisTemplate.opsForValue()
                .increment(this.key(key), delta);
    }


    /*
    =====================
        Lock
    =====================
     */


    public RedisLockToken tryLock(String key, Duration timeout){


        String value = UUID.randomUUID().toString().replace("-","");

        String redisKey = this.key(key);

        Boolean success = stringRedisTemplate
                .opsForValue()
                .setIfAbsent(
                        redisKey,
                        value,
                        timeout
                );

        if(Boolean.TRUE.equals(success)){
            return new RedisLockToken(redisKey, value);
        }

        return null;
    }

    public boolean unlock(RedisLockToken token){
        if(token == null){
            return false;
        }

        Long result = stringRedisTemplate.execute(
                        UNLOCK_SCRIPT,
                        List.of(token.key()),
                        token.value()
                );


        return result != null && result > 0;
    }



    /*
    =====================
        Common
    =====================
     */


    public boolean delete(String key){

        return Boolean.TRUE.equals(
                redisTemplate.delete(this.key(key))
        );
    }


    public Long delete(Collection<String> keys){

        List<String> redisKeys =
                keys.stream()
                        .map(this::key)
                        .toList();

        return redisTemplate.delete(redisKeys);
    }



    public boolean exists(String key){

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(
                        this.key(key)
                )
        );
    }




    public boolean expire(String key, Duration timeout){

        return Boolean.TRUE.equals(
                redisTemplate.expire(
                        this.key(key),
                        timeout
                )
        );
    }




    public long ttl(String key, TimeUnit unit){

        Long ttl = redisTemplate.getExpire(this.key(key), unit);

        return ttl == null ? -1 : ttl;
    }






    public Set<String> scan(String pattern){


        Set<String> result = new HashSet<>();


        String prefix = prefix();



        redisTemplate.execute(
                (RedisCallback<Void>) connection -> {

                    try(Cursor<byte[]> cursor =
                                connection.keyCommands()
                                        .scan(
                                                ScanOptions.scanOptions()
                                                        .match(this.key(pattern))
                                                        .count(500)
                                                        .build()
                                        )){

                        while(cursor.hasNext()){


                            String redisKey = new String(cursor.next(), StandardCharsets.UTF_8);

                            if(!prefix.isEmpty()
                                    && redisKey.startsWith(prefix)){

                                result.add(
                                        redisKey.substring(prefix.length())
                                );
                            }else{
                                result.add(redisKey);
                            }
                        }
                    }


                    return null;
                }
        );


        return result;
    }

}
