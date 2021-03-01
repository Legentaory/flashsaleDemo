package com.wentaodemos.flashsale.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;

@Slf4j
@Service
public class RedisService {

    @Resource
    private JedisPool jedisPool;

    public void setValue(String key, Long value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value.toString());
        jedisClient.close();
    }

    public void setValue(String key, String value){
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value);
        jedisClient.close();
    }

    public String getValue(String key) {
        Jedis jedisClient = jedisPool.getResource();
        String value = jedisClient.get(key);
        jedisClient.close();
        return value;
    }

    public boolean stockDeductValidator(String key) {
        try(Jedis jedisClient = jedisPool.getResource()){
            String script = "if redis.call('exists',KEYS[1]) == 1 then\n" +
                                    " local stock = tonumber(redis.call('get', KEYS[1]))\n" +
                                    " if( stock <=0 ) then\n" +
                                        " return -1\n" +
                                    " end;\n" +
                                    " redis.call('decr',KEYS[1]);\n" +
                                    " return stock - 1;\n" +
                                " end;\n" +
                                " return -1;";

            Long stock = (Long) jedisClient.eval(script, Collections.singletonList(key), Collections.emptyList());
            if (stock < 0) {
                return false;
            } else {
                log.info("Redis stock deduction succeeded");
            }
            return true;
        } catch (Throwable throwable) {
            log.error("Stock deduction failed, check error message: " + throwable.toString());
            return false;
        }
    }

    public void revertStock(String key) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.incr(key);
        jedisClient.close();
    }

    public boolean isInLimitMember(long activityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        boolean sismember = jedisClient.sismember("activityUsers:" + activityId, String.valueOf(userId));
        log.info("userId: {} has engaged in activityId: {}  {}", userId, activityId, sismember);
        return sismember;
    }

    public void addLimitMember(long activityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd("activityUsers:" + activityId, String.valueOf(userId));
        jedisClient.close();
    }

    public void removeLimitMember(long activityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem("activityUsers:" + activityId, String.valueOf(userId));
        jedisClient.close();
    }

    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        Jedis jedisClient = jedisPool.getResource();
        String result = jedisClient.set(lockKey, requestId, "NX", "PX", expireTime);
        jedisClient.close();
        if ("OK".equals(result)) {
            return true;
        }
        return false;
    }

    public boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedisClient = jedisPool.getResource();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = (Long) jedisClient.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        jedisClient.close();
        if (result == 1L) {
            return true;
        }
        return false;
    }
}
