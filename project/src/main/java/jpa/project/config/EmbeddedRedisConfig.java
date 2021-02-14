package jpa.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("local")
public class EmbeddedRedisConfig {
   @Value("${spring.redis.port}")
    private int redisPort;

   private RedisServer redisServer;

   @PostConstruct
    public void redisServer(){
       redisServer=new RedisServer(redisPort);
       redisServer.start();
   }
   @PreDestroy
    public void stopRedis(){
       if(redisServer!=null){
           redisServer.stop();
       }
   }
}
