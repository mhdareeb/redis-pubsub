package org.example;

import redis.clients.jedis.RedisClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PubSub {

    static void main(String[] args) throws IOException {
        RedisConfig redisConfig = new RedisConfig(args);
        try (RedisClient client = new RedisClient.Builder().fromURI(redisConfig.getURI()).build()) {
            if (redisConfig.isPublisher()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    System.out.println("Enter message: ");
                    String message = in.readLine();
                    if (message == null || message.isEmpty()) {
                        break;
                    }
                    for (String channel : redisConfig.getChannels()) {
                        client.publish(channel, message);
                        System.out.println("Message published to channel: " + channel);
                    }
                }
            } else if (redisConfig.isSubscriber()) {
                System.out.println("Redis subscriber listening on channels: " + String.join(", ", redisConfig.getChannels()));
                client.subscribe(new Worker(), redisConfig.getChannels());
            }
        }
    }
}