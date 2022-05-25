package world.worldblocks.redis;

import world.worldblocks.configuration.WBConfig;
import world.worldblocks.cache.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RedisServiceIntegrationTest {

    public RedisServiceIntegrationTest() {}

    @Mock
    public WBConfig wbConfig;

    @Test
    public void testRedisConnect() {
        Mockito.when(wbConfig.getRedisIp()).thenReturn("127.18.0.1");
        Mockito.when(wbConfig.getRedisPort()).thenReturn(6379);
        Mockito.when(wbConfig.getRedisDb()).thenReturn(0);
        Mockito.when(wbConfig.getRedisUser()).thenReturn("");
        Mockito.when(wbConfig.getRedisPass()).thenReturn("");

        RedisService module = new RedisService();
        module.init(wbConfig);
        RedisClient redisClient = module.getClient();
        redisClient.connect();
    }

    @Test
    public void testStuff() {
        String serverId = "wb2_server1";
        Mockito.when(wbConfig.getRedisIp()).thenReturn("127.18.0.1");
        Mockito.when(wbConfig.getRedisPort()).thenReturn(6379);
        Mockito.when(wbConfig.getRedisDb()).thenReturn(0);
        Mockito.when(wbConfig.getRedisUser()).thenReturn("");
        Mockito.when(wbConfig.getRedisPass()).thenReturn("");

        RedisService module = new RedisService();
        module.init(wbConfig);
        RedisClient redisClient = module.getClient();
        redisClient.connect();

        redisClient.getJedis().sadd("server:" + serverId + ":players", "PlayerID");
        redisClient.getJedis().sadd("server:" + serverId + ":players", "PlayerID2");
        redisClient.getJedis().sadd("server:" + serverId + ":players", "PlayerID3");
        redisClient.getJedis().sadd("server:" + serverId + ":players", "PlayerID5");

        redisClient.getJedis().set("player:" + "PlayerID" + ":server", serverId);
        redisClient.getJedis().set("player:" + "PlayerID2" + ":server", serverId);
        redisClient.getJedis().set("player:" + "PlayerID3" + ":server", serverId);
        redisClient.getJedis().set("player:" + "PlayerID5" + ":server", serverId);
    }
}