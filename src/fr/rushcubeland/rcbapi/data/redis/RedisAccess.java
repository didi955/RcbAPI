package fr.rushcubeland.rcbapi.data.redis;

import org.bukkit.Bukkit;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedisAccess {

    public static RedisAccess INSTANCE;

    private RedissonClient redissonClient;

    public RedisAccess(RedisCredentials redisCredentials) {
        INSTANCE = this;
        this.redissonClient = initRedisson(redisCredentials);
        Bukkit.getLogger().info("Connection established with Redis Server");
    }

    public static void init(){
        new RedisAccess(new RedisCredentials("127.0.0.1", "dh57je24&*", 6379));
    }

    public static void close(){
        RedisAccess.INSTANCE.getRedissonClient().shutdown();

    }

    public RedissonClient initRedisson(RedisCredentials redisCredentials){
        final Config config = new Config();

        config.setCodec(new JsonJacksonCodec());
        //config.setUseLinuxNativeEpoll(true);
        config.setThreads(8);
        config.setNettyThreads(8);
        config.useSingleServer()
                .setAddress(redisCredentials.toRedisURL())
                .setPassword(redisCredentials.getPassword())
                .setDatabase(3)
                .setClientName(redisCredentials.getClientName());

        return Redisson.create(config);

    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
