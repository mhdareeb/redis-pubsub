package org.example;

import redis.clients.jedis.JedisPubSub;

public class Worker extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("Received message | '" + message + "' | on channel '" + channel + "'");
    }
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("Subscribed to channel '" + channel + "' | " + subscribedChannels + " channels subscribed");
    }
}

