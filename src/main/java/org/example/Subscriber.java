package org.example;

import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.RedisClient;

public class Subscriber extends JedisPubSub {
    private final String redisURI;
    private final String[] channels;

    public Subscriber(String redisURI, String[] channels) {
        this.redisURI = redisURI;
        this.channels = channels;
    }

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("Received message | '" + message + "' | on channel '" + channel + "'");
    }
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("Subscribed to channel '" + channel + "' | " + subscribedChannels + " channels subscribed");
    }

    public void start() {
        try (RedisClient client = new RedisClient.Builder().fromURI(redisURI).build()) {
            System.out.println("Subscribing to channels: " + String.join(", ", channels));
            client.subscribe(this, channels);
        }
    }
}
