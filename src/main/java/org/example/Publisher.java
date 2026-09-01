package org.example;

import redis.clients.jedis.RedisClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Publisher {

    private final String redisURI;
    private final String[] channels;

    public Publisher(String redisURI, String[] channels) {
        this.redisURI = redisURI;
        this.channels = channels;
    }

    public void start() {
        try (RedisClient client = new RedisClient.Builder().fromURI(redisURI).build()) {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Enter message: ");
                String message = in.readLine();
                if (message == null || message.isEmpty()) {
                    break;
                }
                for (String channel : channels) {
                    client.publish(channel, message);
                    System.out.println("Message published to channel: " + channel);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
