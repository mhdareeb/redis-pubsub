package org.example;

public class PubSub {

    static void main(String[] args) {
        RedisConfig redisConfig = new RedisConfig(args);
        if (redisConfig.isPublisher()) {
            new Publisher(redisConfig.getURI(), redisConfig.getChannels()).start();
        } else if (redisConfig.isSubscriber()) {
            new Subscriber(redisConfig.getURI(), redisConfig.getChannels()).start();
        }
    }
}