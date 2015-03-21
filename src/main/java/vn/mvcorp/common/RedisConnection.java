package vn.mvcorp.common;

/**
 * Created by tienbm on 06/10/2014.
 */

import redis.clients.jedis.Jedis;

public class RedisConnection {
    private Jedis jedis;

    public RedisConnection() {
        jedis = new Jedis(Config.REDIS_HOST, Config.REDIS_PORT);
        jedis.connect();
    }

    public String getFBToken(String nodeId) {
        return jedis.hget("fbtokens", nodeId);
    }

    public void addFBToken(String nodeId, String token) {
        jedis.hset("fbtokens", nodeId, token);

    }

    public void addCrawlInfo(String infoType, String value) {
        jedis.set(infoType, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }
//    public static void main(String[] args){
//        RedisConnection redisConnection = new RedisConnection();
//        redisConnection.addFBToken("123","345");
//    }

}
